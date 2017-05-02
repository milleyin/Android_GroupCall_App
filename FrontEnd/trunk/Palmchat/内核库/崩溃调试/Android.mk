# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)
JNI_PATH := $(LOCAL_PATH)
include $(CLEAR_VARS)
#LOCAL_CPPFLAGS += -frtti

#LOCAL_CFLAGS += -fstrict-aliasing -Wstrict-aliasing=2 -fno-align-jumps
#LOCAL_CFLAGS += -DUSE_INDIRECT_REF
LOCAL_CFLAGS += -Wall -Wextra -Wno-unused-parameter
LOCAL_CFLAGS += -DARCH_VARIANT=\"$(dvm_arch_variant)\"
 LOCAL_CFLAGS += -DWITH_INSTR_CHECKS
  LOCAL_CFLAGS += -DWITH_EXTRA_OBJECT_VALIDATION
  LOCAL_CFLAGS += -DWITH_TRACKREF_CHECKS
  LOCAL_CFLAGS += -DWITH_ALLOC_LIMITS
  LOCAL_CFLAGS += -DWITH_EXTRA_GC_CHECKS=1
  #LOCAL_CFLAGS += -DCHECK_MUTEX
  #LOCAL_CFLAGS += -DPROFILE_FIELD_ACCESS
  LOCAL_CFLAGS += -DDVM_SHOW_EXCEPTION=3
  LOCAL_CFLAGS += -DEASY_GDB
LOCAL_CFLAGS += -UNDEBUG -DDEBUG=1 -DLOG_NDEBUG=1 -DWITH_DALVIK_ASSERT
LOCAL_CFLAGS += -DWITH_JNI_STACK_CHECK

LOCAL_LDLIBS += -L/Users/Stone/Desktop/work/Android/mac-android/android-ndk-r8e/platforms/android-14/arch-arm/usr/lib -llog

LOCAL_LDLIBS+=-lsqlite
LOCAL_MODULE    := afmobi
LOCAL_STATIC_LIBRARIES += afmobi

BASE64_SRC_DIR := src/base64
CORE_SRC_DIR := src/core
DATABASE_SRC_DIR := src/database
DEBUG_SRC_DIR := src/debug
JSON_SRC_DIR := src/json
MD5_SRC_DIR := src/md5
NET_SRC_DIR := src/net
TIMER_SRC_DIR := src/timer
UTIL_SRC_DIR :=src/util
PLATFORM_DIR:=platform

LOCAL_C_INCLUDES := $(JNI_PATH)/include $(JNI_PATH)/inc	$(JNI_PATH)/src/base64 $(JNI_PATH)/platform
LOCAL_SRC_FILES := $(UTIL_SRC_DIR)/afmobi_array.c \
$(UTIL_SRC_DIR)/afmobi_buffer.c \
$(UTIL_SRC_DIR)/afmobi_common.c \
$(UTIL_SRC_DIR)/afmobi_list.c \
$(UTIL_SRC_DIR)/afmobi_res.c \
$(UTIL_SRC_DIR)/afmobi_utility.c \
$(UTIL_SRC_DIR)/afmobi_hash.c \
$(DATABASE_SRC_DIR)/afmobi_database_message.c \
$(DATABASE_SRC_DIR)/afmobi_data.c \
$(DATABASE_SRC_DIR)/afmobi_database.c \
$(DATABASE_SRC_DIR)/afmobi_database_friend.c \
$(DATABASE_SRC_DIR)/afmobi_database_login_info.c \
$(DATABASE_SRC_DIR)/afmobi_database_media.c \
$(DATABASE_SRC_DIR)/afmobi_database_profile_info.c \
$(DATABASE_SRC_DIR)/afmobi_database_grp_profile_info.c \
$(DATABASE_SRC_DIR)/afmobi_database_image.c \
$(DATABASE_SRC_DIR)/afmobi_database_voice.c \
$(DATABASE_SRC_DIR)/afmobi_database_recent_message.c \
$(DATABASE_SRC_DIR)/afmobi_database_contact.c \
$(BASE64_SRC_DIR)/afmobi_des_encrypt.c \
$(BASE64_SRC_DIR)/afmobi_base64.c \
$(BASE64_SRC_DIR)/afmobi_crc32.c \
$(NET_SRC_DIR)/afmobi_http.c \
$(NET_SRC_DIR)/afmobi_http_constant.c \
$(NET_SRC_DIR)/afmobi_http_common.c \
$(NET_SRC_DIR)/afmobi_http_friend.c \
$(NET_SRC_DIR)/afmobi_http_group.c \
$(NET_SRC_DIR)/afmobi_http_interface.c \
$(NET_SRC_DIR)/afmobi_http_login.c \
$(NET_SRC_DIR)/afmobi_http_manager.c \
$(NET_SRC_DIR)/afmobi_http_media.c \
$(NET_SRC_DIR)/afmobi_http_message.c \
$(NET_SRC_DIR)/afmobi_http_polling.c \
$(NET_SRC_DIR)/afmobi_http_queue.c \
$(NET_SRC_DIR)/afmobi_http_search.c \
$(NET_SRC_DIR)/afmobi_http_avatar.c \
$(NET_SRC_DIR)/afmobi_http_chatroom.c \
$(JSON_SRC_DIR)/afmobi_json.c \
$(MD5_SRC_DIR)/afmobi_md5.c \
$(CORE_SRC_DIR)/afmobi_core.c \
$(DEBUG_SRC_DIR)/afmobi_mem_debug.c \
$(PLATFORM_DIR)/afmobi_fs.cpp \
$(PLATFORM_DIR)/afmobi_os.cpp \
$(PLATFORM_DIR)/afmobi_socket.cpp \
$(PLATFORM_DIR)/afmobi_string.cpp \
$(PLATFORM_DIR)/afmobi_debug.cpp \
$(PLATFORM_DIR)/afmobi_sql.cpp \
$(PLATFORM_DIR)/com_core_AfPalmchat.cpp	\
$(PLATFORM_DIR)/jni_comm.cpp	\
$(PLATFORM_DIR)/afmobi_timer.cpp	\
$(PLATFORM_DIR)/jni_crash_signal.cpp


#$(PLATFORM_DIR)/sqlite3.c 
LOCAL_C_INCLUDES += $(PLATFORM_DIR)/sqlite3.h
include $(BUILD_SHARED_LIBRARY)







