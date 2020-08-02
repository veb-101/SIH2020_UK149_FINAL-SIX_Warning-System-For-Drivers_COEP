import sys
import os
import argparse
import time

import cv2
print(cv2.__version__)


def extractImages():
    folders = ['01', "02", "03", "04", "05", "06", "07", "08"]

    for pathOut in folders:
        print('folder:', pathOut)
        if not os.path.isdir(pathOut):
            os.makedirs(pathOut)

        count = 0
        vidcap = cv2.VideoCapture(f'{pathOut}.mp4')
        success, image = vidcap.read()
        success = True
        while success:
            # added this line
            vidcap.set(cv2.CAP_PROP_POS_MSEC, (count*2000))
            success, image = vidcap.read()
            print('\rRead a new frame: ', success, end="")
            try:
                cv2.imwrite(pathOut + "\\frame%d.jpg" %
                            count, image)     # save frame as JPEG file
            except:
                print("\nDone")
                break
            count = count + 1
        vidcap.release()
        cv2.destroyAllWindows()
        time.sleep(2)


if __name__ == "__main__":
    extractImages()
