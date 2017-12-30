TEMPLATE = app
TARGET = Ember-Qt
VERSION = 1.1.5.2
INCLUDEPATH += src src/json src/qt
QT += network
DEFINES += ENABLE_WALLET
DEFINES += BOOST_THREAD_USE_LIB
DEFINES += BOOST_SPIRIT_THREADSAFE
win32:DEFINES += BOOST_NO_CXX11_SCOPED_ENUMS
win32:DEFINES += BOOST_THREAD_PROVIDES_GENERIC_SHARED_MUTEX_ON_WIN
DEFINES += QT_GUI
DEFINES += __NO_SYSTEM_INCLUDES
CONFIG += no_include_pwd
CONFIG += thread
win32:CONFIG += static
QMAKE_CXXFLAGS += -std=c++11

DESTDIR_TARGET = $${DESTDIR}$${TARGET}.exe

OBJECTS_DIR = build
MOC_DIR = build
RCC_DIR = build
UI_DIR = build

greaterThan(QT_MAJOR_VERSION, 4) {
    QT += widgets
    DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0
}

CONFIG(release, debug|release) {
    DESTDIR = release
    contains(RELEASE, 1) {
        #win32:QMAKE_POST_LINK += upx -k --best --overlay=strip --strip-relocs=0 --compress-icons=2 $(DESTDIR_TARGET)
        #win32:QMAKE_POST_LINK += && C:/PROGRA~2/WI3CF2~1/8.1/bin/x64/mt.exe -manifest src/qt/res/$(TARGET).manifest -outputresource:$(DESTDIR_TARGET);$${LITERAL_HASH}1
        #win32:QMAKE_POST_LINK += signtool.exe sign /t http://timestamp.verisign.com/scripts/timstamp.dll /f "MyCert.pfx" /p MyPassword /d $(DESTDIR_TARGET) $(DESTDIR_TARGET)
    }
    win32:QMAKE_CXXFLAGS_RELEASE -= -O2
    win32:QMAKE_CXXFLAGS_RELEASE = -Os
    win32:QMAKE_CFLAGS_RELEASE -= -O2
    win32:QMAKE_CFLAGS_RELEASE = -Os
} else {
    DESTDIR = debug
    win32:QMAKE_CXXFLAGS_DEBUG -= -O2
    win32:QMAKE_CXXFLAGS_DEBUG = -O0 -fno-omit-frame-pointer -g -gdwarf-2 -ggdb
    win32:QMAKE_CFLAGS_DEBUG -= -O2
    win32:QMAKE_CFLAGS_DEBUG = -O0 -fno-omit-frame-pointer -g -gdwarf-2 -ggdb
}

!win32 {
        # for extra security against potential buffer overflows: enable GCCs Stack Smashing Protection
        QMAKE_CXXFLAGS *= -fstack-protector-all --param ssp-buffer-size=1
        QMAKE_LFLAGS *= -fstack-protector-all --param ssp-buffer-size=1
        # We need to exclude this for Windows cross compile with MinGW 4.2.x, as it will result in a non-working executable!
        # This can be enabled for Windows, when we switch to MinGW >= 4.4.x.
}
# for extra security on Windows: enable ASLR and DEP via GCC linker flags
win32:QMAKE_LFLAGS_RELEASE *= -Wl,--dynamicbase -Wl,--nxcompat
win32:QMAKE_LFLAGS_RELEASE += -static-libgcc -static-libstdc++

# use: qmake "RELEASE=1"
contains(RELEASE, 1) {
    # Mac: compile for maximum compatibility (10.7, 32-bit)
    macx:QMAKE_CXXFLAGS += -mmacosx-version-min=10.7 -arch x86_64 -isysroot /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk

    linux {
        message(RELEASE=1)
        # Linux: static link
        LIBS += -Wl,-Bstatic
    }
}

contains(USE_O3, 1) {
    message(Building O3 optimization flag)
    QMAKE_CXXFLAGS_RELEASE -= -O2
    QMAKE_CFLAGS_RELEASE -= -O2
    win32:QMAKE_CXXFLAGS_RELEASE -= -Os
    win32:QMAKE_CFLAGS_RELEASE -= -Os
    QMAKE_CXXFLAGS += -O3
    QMAKE_CFLAGS += -O3
}

*-g++-32 {
    message("32 platform, adding -msse2 flag")

    QMAKE_CXXFLAGS += -msse2
    QMAKE_CFLAGS += -msse2
}

QMAKE_CXXFLAGS_WARN_ON = -fdiagnostics-show-option -Wall -Wextra -Wno-ignored-qualifiers -Wformat -Wformat-security -Wno-unused-parameter -Wstack-protector

isEmpty(MINIUPNPC_INCLUDE_PATH) {
    macx:MINIUPNPC_INCLUDE_PATH = /usr/local/Cellar/miniupnpc/2.0.20170509/include
    win32:MINIUPNPC_INCLUDE_PATH=$$PWD/deps/miniupnpc
}

isEmpty(MINIUPNPC_LIB_PATH) {
    macx:MINIUPNPC_LIB_PATH = /usr/local/Cellar/miniupnpc/2.0.20170509/lib
    win32:MINIUPNPC_LIB_PATH=$$PWD/deps/miniupnpc/miniupnpc
}

# platform specific defaults, if not overridden on command line
isEmpty(BOOST_LIB_SUFFIX) {
    macx:BOOST_LIB_SUFFIX = -mt
    win32:BOOST_LIB_SUFFIX = -mgw53-mt-s-1_65_1
}

isEmpty(OPENSSL_INCLUDE_PATH) {
    macx:OPENSSL_INCLUDE_PATH = /usr/local/Cellar/openssl/1.0.2l/include
    win32:OPENSSL_INCLUDE_PATH=$$PWD/deps/libressl-2.6.3/include
}

isEmpty(OPENSSL_LIB_PATH) {
    macx:OPENSSL_LIB_PATH = /usr/local/Cellar/openssl/1.0.2l/lib
    win32:OPENSSL_LIB_PATH=$$PWD/deps/libressl-2.6.3/lib
}

isEmpty(BDB_LIB_PATH) {
    macx:BDB_LIB_PATH = /usr/local/Cellar/berkeley-db/6.2.32/lib
    linux:BDB_INCLUDE_PATH = /usr/local/BerkeleyDB6.2/lib
    win32:BDB_LIB_PATH=$$PWD/deps/db-6.1.26/build_unix
}

isEmpty(BDB_LIB_SUFFIX) {
    macx:BDB_LIB_SUFFIX = -6.2
}

isEmpty(BDB_INCLUDE_PATH) {
    macx:BDB_INCLUDE_PATH = /usr/local/Cellar/berkeley-db/6.2.32/include
    linux:BDB_INCLUDE_PATH = /usr/local/BerkeleyDB6.2/include
    win32:BDB_INCLUDE_PATH=$$PWD/deps/db-6.1.26/build_unix
}

isEmpty(BOOST_LIB_PATH) {
    macx:BOOST_LIB_PATH = /usr/local/Cellar/boost/1.65.1/lib
    win32:BOOST_LIB_PATH=$$PWD/deps/boost_1_65_1/stage/lib
}

isEmpty(BOOST_INCLUDE_PATH) {
    macx:BOOST_INCLUDE_PATH = /usr/local/Cellar/boost/1.65.1/include
    win32:BOOST_INCLUDE_PATH=$$PWD/deps/boost_1_65_1
}

# use: qmake "USE_QRCODE=1"
# libqrencode (http://fukuchi.org/works/qrencode/index.en.html) must be installed for support
contains(USE_QRCODE, 1) {
    message(Building with QRCode support)
    DEFINES += USE_QRCODE
    LIBS += -lqrencode
    QRENCODE_INCLUDE_PATH=$$PWD/deps/qrencode-3.4.4
    QRENCODE_LIB_PATH=$$PWD/deps/qrencode-3.4.4/.libs
}

# use: qmake "USE_UPNP=1" ( enabled by default; default)
#  or: qmake "USE_UPNP=0" (disabled by default)
#  or: qmake "USE_UPNP=-" (not supported)
# miniupnpc (http://miniupnp.free.fr/files/) must be installed for support
contains(USE_UPNP, -) {
    message(Building without UPNP support)
} else {
    message(Building with UPNP support)
    count(USE_UPNP, 0) {
        USE_UPNP=1
    }
    DEFINES += USE_UPNP=$$USE_UPNP MINIUPNP_STATICLIB STATICLIB
    INCLUDEPATH += $$MINIUPNPC_INCLUDE_PATH
    LIBS += $$join(MINIUPNPC_LIB_PATH,,-L,) -lminiupnpc
    win32:LIBS += -liphlpapi
}

# use: qmake "USE_DBUS=1" or qmake "USE_DBUS=0"
linux:count(USE_DBUS, 0) {
    message(USE_DBUS=1)
    USE_DBUS=1
}
contains(USE_DBUS, 1) {
    message(Building with DBUS (Freedesktop notifications) support)
    DEFINES += USE_DBUS
    QT += dbus
}

contains(BITCOIN_NEED_QT_PLUGINS, 1) {
    DEFINES += BITCOIN_NEED_QT_PLUGINS
    QTPLUGIN += qcncodecs qjpcodecs qtwcodecs qkrcodecs qtaccessiblewidgets
}

#Build latest libdb 
#Since windows build seems to be ok we'll only add this on linux builds
!win32 {
    INCLUDEPATH += src/libdb/build_unix
    genlibdb.commands = cd $$PWD/src/libdb/build_unix && CC=$$QMAKE_CC CXX=$$QMAKE_CXX ../dist/configure --enable-cxx && $(MAKE) OPT=\"$$QMAKE_CXXFLAGS $$QMAKE_CXXFLAGS_RELEASE\"    

}
!win32 {
    LIBS += $$PWD/src/libdb/build_unix/libdb_cxx.a $$PWD/src/libdb/build_unix/libdb.a
}
genlibdb.target = $$PWD/src/libdb/libdb_cxx.a
genlibdb.depends = FORCE
QMAKE_EXTRA_TARGETS += genlibdb
QMAKE_CLEAN += cd $$PWD/src/libdb/ && $(MAKE) clean

INCLUDEPATH += src/leveldb/include src/leveldb/helpers
LIBS += $$PWD/src/leveldb/libleveldb.a $$PWD/src/leveldb/libmemenv.a
SOURCES += src/txdb-leveldb.cpp \
    src/aes_helper.c \
    src/blake.c \
    src/bmw.c \
    src/cubehash.c \
    src/echo.c \
    src/groestl.c \
    src/jh.c \
    src/keccak.c \
    src/luffa.c \
    src/shavite.c \
    src/simd.c \
    src/skein.c \
    src/fugue.c \
    src/hamsi.c

!win32 {
    # we use QMAKE_CXXFLAGS_RELEASE even without RELEASE=1 because we use RELEASE to indicate linking preferences not -O preferences
    genleveldb.commands = cd $$PWD/src/leveldb && CC=$$QMAKE_CC CXX=$$QMAKE_CXX $(MAKE) OPT=\"$$QMAKE_CXXFLAGS $$QMAKE_CXXFLAGS_RELEASE\" libleveldb.a libmemenv.a
} else {
    # make an educated guess about what the ranlib command is called
    isEmpty(QMAKE_RANLIB) {
        QMAKE_RANLIB = $$replace(QMAKE_STRIP, strip, ranlib)
    }
    LIBS += -lshlwapi
    # Disable if building on Windows
    #genleveldb.commands = cd $$PWD/src/leveldb && $(MAKE) CC=$$QMAKE_CC CXX=$$QMAKE_CXX TARGET_OS=OS_WINDOWS_CROSSCOMPILE OPT=\"$$QMAKE_CXXFLAGS $$QMAKE_CXXFLAGS_RELEASE\" libleveldb.a libmemenv.a && $$QMAKE_RANLIB $$PWD/src/leveldb/libleveldb.a && $$QMAKE_RANLIB $$PWD/src/leveldb/libmemenv.a
}
genleveldb.target = $$PWD/src/leveldb/libleveldb.a
!win32 {
    genleveldb.depends = genlibdb    
} else {
    genleveldb.depends = FORCE
}

PRE_TARGETDEPS += $$PWD/src/leveldb/libleveldb.a
QMAKE_EXTRA_TARGETS += genleveldb
# Gross ugly hack that depends on qmake internals, unfortunately there is no other way to do it.
!win32:QMAKE_CLEAN += $$PWD/src/leveldb/libleveldb.a; cd $$PWD/src/leveldb && $(MAKE) clean

# regenerate src/build.h
!windows|contains(USE_BUILD_INFO, 1) {
    genbuild.depends = FORCE
    genbuild.commands = cd $$PWD; /bin/sh share/genbuild.sh $$OUT_PWD/build/build.h
    genbuild.target = $$OUT_PWD/build/build.h
    PRE_TARGETDEPS += $$OUT_PWD/build/build.h
    QMAKE_EXTRA_TARGETS += genbuild
    DEFINES += HAVE_BUILD_INFO
}

# Input
DEPENDPATH += src src/json src/qt
HEADERS += src/qt/bitcoingui.h \
    src/qt/transactiontablemodel.h \
    src/qt/addresstablemodel.h \
    src/qt/optionsdialog.h \
    src/qt/coincontroldialog.h \
    src/qt/coincontroltreewidget.h \
    src/qt/sendcoinsdialog.h \
    src/qt/addressbookpage.h \
    src/qt/signverifymessagedialog.h \
    src/qt/aboutdialog.h \
    src/qt/editaddressdialog.h \
    src/qt/bitcoinaddressvalidator.h \
    src/alert.h \
    src/addrman.h \
    src/base58.h \
    src/bignum.h \
    src/chainparams.h \
    src/chainparamsseeds.h \
    src/checkpoints.h \
    src/compat.h \
    src/coincontrol.h \
    src/sync.h \
    src/util.h \
    src/hash.h \
    src/uint256.h \
    src/kernel.h \
    src/scrypt.h \
    src/pbkdf2.h \
    src/serialize.h \
    src/core.h \
    src/main.h \
    src/miner.h \
    src/net.h \
    src/key.h \
    src/db.h \
    src/txdb.h \
    src/txmempool.h \
    src/walletdb.h \
    src/script.h \
    src/init.h \
    src/mruset.h \
    src/json/json_spirit_writer_template.h \
    src/json/json_spirit_writer.h \
    src/json/json_spirit_value.h \
    src/json/json_spirit_utils.h \
    src/json/json_spirit_stream_reader.h \
    src/json/json_spirit_reader_template.h \
    src/json/json_spirit_reader.h \
    src/json/json_spirit_error_position.h \
    src/json/json_spirit.h \
    src/qt/clientmodel.h \
    src/qt/guiutil.h \
    src/qt/transactionrecord.h \
    src/qt/guiconstants.h \
    src/qt/optionsmodel.h \
    src/qt/monitoreddatamapper.h \
    src/qt/trafficgraphwidget.h \
    src/qt/transactiondesc.h \
    src/qt/transactiondescdialog.h \
    src/qt/bitcoinamountfield.h \
    src/wallet.h \
    src/keystore.h \
    src/qt/transactionfilterproxy.h \
    src/qt/transactionview.h \
    src/qt/walletmodel.h \
    src/rpcclient.h \
    src/rpcprotocol.h \
    src/rpcserver.h \
    src/timedata.h \
    src/qt/overviewpage.h \
    src/qt/csvmodelwriter.h \
    src/crypter.h \
    src/qt/sendcoinsentry.h \
    src/qt/qvalidatedlineedit.h \
    src/qt/bitcoinunits.h \
    src/qt/qvaluecombobox.h \
    src/qt/askpassphrasedialog.h \
    src/protocol.h \
    src/qt/notificator.h \
    src/qt/paymentserver.h \
    src/allocators.h \
    src/ui_interface.h \
    src/qt/rpcconsole.h \
    src/version.h \
    src/netbase.h \
    src/clientversion.h \
    src/threadsafety.h \
    src/tinyformat.h

SOURCES += src/qt/bitcoin.cpp src/qt/bitcoingui.cpp \
    src/qt/transactiontablemodel.cpp \
    src/qt/addresstablemodel.cpp \
    src/qt/optionsdialog.cpp \
    src/qt/sendcoinsdialog.cpp \
    src/qt/coincontroldialog.cpp \
    src/qt/coincontroltreewidget.cpp \
    src/qt/addressbookpage.cpp \
    src/qt/signverifymessagedialog.cpp \
    src/qt/aboutdialog.cpp \
    src/qt/editaddressdialog.cpp \
    src/qt/bitcoinaddressvalidator.cpp \
    src/alert.cpp \
    src/chainparams.cpp \
    src/version.cpp \
    src/sync.cpp \
    src/txmempool.cpp \
    src/util.cpp \
    src/hash.cpp \
    src/netbase.cpp \
    src/key.cpp \
    src/script.cpp \
    src/core.cpp \
    src/main.cpp \
    src/miner.cpp \
    src/init.cpp \
    src/net.cpp \
    src/checkpoints.cpp \
    src/addrman.cpp \
    src/db.cpp \
    src/walletdb.cpp \
    src/qt/clientmodel.cpp \
    src/qt/guiutil.cpp \
    src/qt/transactionrecord.cpp \
    src/qt/optionsmodel.cpp \
    src/qt/monitoreddatamapper.cpp \
    src/qt/trafficgraphwidget.cpp \
    src/qt/transactiondesc.cpp \
    src/qt/transactiondescdialog.cpp \
    src/qt/bitcoinstrings.cpp \
    src/qt/bitcoinamountfield.cpp \
    src/wallet.cpp \
    src/keystore.cpp \
    src/qt/transactionfilterproxy.cpp \
    src/qt/transactionview.cpp \
    src/qt/walletmodel.cpp \
    src/rpcclient.cpp \
    src/rpcprotocol.cpp \
    src/rpcserver.cpp \
    src/rpcdump.cpp \
    src/rpcmisc.cpp \
    src/rpcnet.cpp \
    src/rpcmining.cpp \
    src/rpcwallet.cpp \
    src/rpcblockchain.cpp \
    src/rpcrawtransaction.cpp \
    src/timedata.cpp \
    src/qt/overviewpage.cpp \
    src/qt/csvmodelwriter.cpp \
    src/crypter.cpp \
    src/qt/sendcoinsentry.cpp \
    src/qt/qvalidatedlineedit.cpp \
    src/qt/bitcoinunits.cpp \
    src/qt/qvaluecombobox.cpp \
    src/qt/askpassphrasedialog.cpp \
    src/protocol.cpp \
    src/qt/notificator.cpp \
    src/qt/paymentserver.cpp \
    src/qt/rpcconsole.cpp \
    src/noui.cpp \
    src/kernel.cpp \
    src/scrypt-arm.S \
    src/scrypt-x86.S \
    src/scrypt-x86_64.S \
    src/scrypt.cpp \
    src/pbkdf2.cpp

RESOURCES += \
    src/qt/bitcoin.qrc

FORMS += \
    src/qt/forms/coincontroldialog.ui \
    src/qt/forms/sendcoinsdialog.ui \
    src/qt/forms/addressbookpage.ui \
    src/qt/forms/signverifymessagedialog.ui \
    src/qt/forms/aboutdialog.ui \
    src/qt/forms/editaddressdialog.ui \
    src/qt/forms/transactiondescdialog.ui \
    src/qt/forms/overviewpage.ui \
    src/qt/forms/sendcoinsentry.ui \
    src/qt/forms/askpassphrasedialog.ui \
    src/qt/forms/rpcconsole.ui \
    src/qt/forms/optionsdialog.ui

contains(USE_QRCODE, 1) {
        HEADERS += src/qt/qrcodedialog.h
        SOURCES += src/qt/qrcodedialog.cpp
        FORMS += src/qt/forms/qrcodedialog.ui
}

CODECFORTR = UTF-8

# for lrelease/lupdate
# also add new translations to src/qt/bitcoin.qrc under translations/
TRANSLATIONS = $$files(src/qt/locale/bitcoin_*.ts)

isEmpty(QMAKE_LRELEASE) {
    win32:QMAKE_LRELEASE = $$[QT_INSTALL_BINS]\\lrelease.exe
    else:QMAKE_LRELEASE = $$[QT_INSTALL_BINS]/lrelease
}
isEmpty(QM_DIR):QM_DIR = $$PWD/src/qt/locale
# automatically build translations, so they can be included in resource file
TSQM.name = lrelease ${QMAKE_FILE_IN}
TSQM.input = TRANSLATIONS
TSQM.output = $$QM_DIR/${QMAKE_FILE_BASE}.qm
TSQM.commands = $$QMAKE_LRELEASE ${QMAKE_FILE_IN} -qm ${QMAKE_FILE_OUT}
win32:TSQM.CONFIG = no_link target_predeps
else:TSQM.CONFIG = no_link
QMAKE_EXTRA_COMPILERS += TSQM

# "Other files" to show in Qt Creator
OTHER_FILES += \
    doc/*.rst doc/*.txt doc/README README.md res/Ember-Qt.rc

win32:DEFINES += WIN32
win32:RC_FILE = src/qt/res/Ember-Qt.rc
win32:QMAKE_RC = $${CROSS_COMPILE}windres --use-temp-file

win32:!contains(MINGW_THREAD_BUGFIX, 0) {
    # At least qmake's win32-g++-cross profile is missing the -lmingwthrd
    # thread-safety flag. GCC has -mthreads to enable this, but it doesn't
    # work with static linking. -lmingwthrd must come BEFORE -lmingw, so
    # it is prepended to QMAKE_LIBS_QT_ENTRY.
    # It can be turned off with MINGW_THREAD_BUGFIX=0, just in case it causes
    # any problems on some untested qmake profile now or in the future.
    DEFINES += _MT BOOST_THREAD_PROVIDES_GENERIC_SHARED_MUTEX_ON_WIN
    QMAKE_LIBS_QT_ENTRY = -lmingwthrd $$QMAKE_LIBS_QT_ENTRY
}
win32:QMAKE_LIBS_QT_ENTRY = -Wl,-Bstatic $$QMAKE_LIBS_QT_ENTRY


macx:HEADERS += src/qt/macnotificationhandler.h src/qt/macdockiconhandler.h
macx:OBJECTIVE_SOURCES += src/qt/macnotificationhandler.mm src/qt/macdockiconhandler.mm
macx:LIBS += -framework Foundation -framework ApplicationServices -framework AppKit
macx:DEFINES += MAC_OSX MSG_NOSIGNAL=0
macx:ICON = src/qt/res/icons/Ember.icns
macx:TARGET = "Ember-Qt"
macx:QMAKE_CFLAGS_THREAD += -pthread
macx:QMAKE_LFLAGS_THREAD += -pthread
macx:QMAKE_CXXFLAGS_THREAD += -pthread
macx:QMAKE_INFO_PLIST = share/qt/Info.plist

# Set libraries and includes at end, to use platform-defined defaults if not overridden

INCLUDEPATH += $$BOOST_INCLUDE_PATH
INCLUDEPATH += $$BDB_INCLUDE_PATH
INCLUDEPATH += $$OPENSSL_INCLUDE_PATH
INCLUDEPATH += $$QRENCODE_INCLUDE_PATH
LIBS += $$join(BOOST_LIB_PATH,,-L,)
LIBS += $$join(BDB_LIB_PATH,,-L,)
LIBS += $$join(OPENSSL_LIB_PATH,,-L,)
LIBS += $$join(QRENCODE_LIB_PATH,,-L,)
LIBS += -lssl
LIBS += -lcrypto
LIBS += -ldb_cxx$$BDB_LIB_SUFFIX
LIBS += -lboost_system$$BOOST_LIB_SUFFIX
LIBS += -lboost_filesystem$$BOOST_LIB_SUFFIX
LIBS += -lboost_program_options$$BOOST_LIB_SUFFIX
LIBS += -lboost_thread$$BOOST_LIB_SUFFIX
win32:LIBS += -lboost_chrono$$BOOST_LIB_SUFFIX
# -lgdi32 has to happen after -lcrypto (see  #681)
win32:LIBS += -lws2_32 -lshlwapi -lmswsock -lole32 -loleaut32 -luuid -lgdi32

contains(RELEASE, 1) {
    linux {
        # Linux: turn dynamic linking back on for c/c++ runtime libraries
        LIBS += -Wl,-Bdynamic
        binfile.files += release/Ember-Qt
        binfile.path = /usr/bin
        icon.files += src/qt/res/icons/Ember_64x64.png
        icon.path = /usr/share/ember-qt/
        shortcut.files += share/linux/ember-qt.desktop
        shortcut.path += /usr/share/applications/

        INSTALLS += binfile
        INSTALLS += icon
        INSTALLS += shortcut
    }
}

linux {
    DEFINES += LINUX
    LIBS += -lrt -ldl
}

system($$QMAKE_LRELEASE -silent $$_PRO_FILE_)
