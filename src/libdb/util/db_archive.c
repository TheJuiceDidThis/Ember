/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 1996, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

#include "db_config.h"

#include "db_int.h"

#ifndef lint
static const char copyright[] =
    "Copyright (c) 1996, 2016 Oracle and/or its affiliates.  All rights reserved.\n";
#endif

int main __P((int, char *[]));
void usage __P((void));
int version_check __P((void));

const char *progname;

int
main(argc, argv)
	int argc;
	char *argv[];
{
	extern char *optarg;
	extern int optind;
	DB_ENV	*dbenv;
	u_int32_t flags;
	int ch, exitval, ret, verbose;
	char **file, *home, **list, *msgpfx, *passwd;

	if ((progname = __db_rpath(argv[0])) == NULL)
		progname = argv[0];
	else
		++progname;

	if ((ret = version_check()) != 0)
		return (ret);

	dbenv = NULL;
	flags = 0;
	verbose = 0;
	exitval = EXIT_SUCCESS;
	home = msgpfx = passwd = NULL;
	file = list = NULL;
	while ((ch = getopt(argc, argv, "adh:lm:P:sVv")) != EOF)
		switch (ch) {
		case 'a':
			LF_SET(DB_ARCH_ABS);
			break;
		case 'd':
			LF_SET(DB_ARCH_REMOVE);
			break;
		case 'h':
			home = optarg;
			break;
		case 'l':
			LF_SET(DB_ARCH_LOG);
			break;
		case 'm':
			msgpfx = optarg;
			break;
		case 'P':
			if (passwd != NULL) {
				fprintf(stderr, DB_STR("5135",
					"Password may not be specified twice"));
				goto err;
			}
			passwd = strdup(optarg);
			memset(optarg, 0, strlen(optarg));
			if (passwd == NULL) {
				fprintf(stderr, DB_STR_A("5119",
				    "%s: strdup: %s\n", "%s %s\n"),
				    progname, strerror(errno));
				goto err;
			}
			break;
		case 's':
			LF_SET(DB_ARCH_DATA);
			break;
		case 'V':
			printf("%s\n", db_version(NULL, NULL, NULL));
			goto done;
		case 'v':
			/*
			 * !!!
			 * The verbose flag no longer actually does anything,
			 * but it's left rather than adding it back at some
			 * future date.
			 */
			verbose = 1;
			break;
		case '?':
		default:
			goto usage_err;
		}
	argc -= optind;
	argv += optind;

	if (argc != 0)
		goto usage_err;

	/* Handle possible interruptions. */
	__db_util_siginit();

	/*
	 * Create an environment object and initialize it for error
	 * reporting.
	 */
	if ((ret = db_env_create(&dbenv, 0)) != 0) {
		fprintf(stderr,
		    "%s: db_env_create: %s\n", progname, db_strerror(ret));
		goto err;
	}

	dbenv->set_errfile(dbenv, stderr);
	dbenv->set_errpfx(dbenv, progname);
	if (msgpfx != NULL)
		dbenv->set_msgpfx(dbenv, msgpfx);

	if (passwd != NULL && (ret = dbenv->set_encrypt(dbenv,
	    passwd, DB_ENCRYPT_AES)) != 0) {
		dbenv->err(dbenv, ret, "set_passwd");
		goto err;
	}
	/*
	 * If attaching to a pre-existing environment fails, create a
	 * private one and try again.
	 */
	if ((ret = dbenv->open(dbenv, home, DB_USE_ENVIRON, 0)) != 0 &&
	    (ret == DB_VERSION_MISMATCH || ret == DB_REP_LOCKOUT ||
	    (ret = dbenv->open(dbenv, home, DB_CREATE |
	    DB_INIT_LOG | DB_PRIVATE | DB_USE_ENVIRON, 0)) != 0)) {
		dbenv->err(dbenv, ret, "DB_ENV->open");
		goto err;
	}

	/* Get the list of names. */
	if ((ret = dbenv->log_archive(dbenv, &list, flags)) != 0) {
		dbenv->err(dbenv, ret, "DB_ENV->log_archive");
		goto err;
	}

	/* Print the list of names. */
	if (list != NULL) {
		for (file = list; *file != NULL; ++file)
			printf("%s\n", *file);
		free(list);
	}

	if (0) {
usage_err:	usage();
err:		exitval = EXIT_FAILURE;
	}
done:	if (dbenv != NULL && (ret = dbenv->close(dbenv, 0)) != 0) {
		exitval = EXIT_FAILURE;
		fprintf(stderr,
		    "%s: dbenv->close: %s\n", progname, db_strerror(ret));
	}

	if (passwd != NULL)
		free(passwd);

	/* Resend any caught signal. */
	__db_util_sigresend();

	return (exitval);
}

void
usage()
{
	(void)fprintf(stderr, "usage: %s %s\n", progname,
	    "[-adlsVv] [-h home] [-m msg_pfx] [-P password]");
}

int
version_check()
{
	int v_major, v_minor, v_patch;

	/* Make sure we're loaded with the right version of the DB library. */
	(void)db_version(&v_major, &v_minor, &v_patch);
	if (v_major != DB_VERSION_MAJOR || v_minor != DB_VERSION_MINOR) {
		fprintf(stderr, DB_STR_A("5120",
		    "%s: version %d.%d doesn't match library version %d.%d\n",
		    "%s %d %d %d %d\n"), progname, DB_VERSION_MAJOR,
		    DB_VERSION_MINOR, v_major, v_minor);
		return (EXIT_FAILURE);
	}
	return (0);
}
