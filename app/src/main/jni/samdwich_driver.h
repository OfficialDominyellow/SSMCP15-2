//
// Created by 김희중 on 2016-01-25.
//

#ifndef SSMCP15_2_SAMDWICH_DRIVER_H
#define SSMCP15_2_SAMDWICH_DRIVER_H

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <linux/input.h>
#include <linux/uinput.h>

#define die(str, args...) do { \
        perror(str); \
        exit(EXIT_FAILURE); \
    } while(0)

static int fd;//uinput fd

#endif //SSMCP15_2_SAMDWICH_DRIVER_H
