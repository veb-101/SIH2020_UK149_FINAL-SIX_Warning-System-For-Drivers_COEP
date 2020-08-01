"""
    A single script to rename .jpg and corresponding .xml file in numerical order, transfer (copy or move) renamed
    images and xmls to a universal folder and remove a file whose corresponding .jpg or .xml file is missing
    
    Usage: python rename_transfer_remove.py
"""

import os
import shutil
import itertools


def rename_files(root_dir, folders):
    for folder in folders:
        xml_files = sorted([file for file in os.listdir(
            folder) if file.endswith('.xml')])

        jpg_files = []
        images_missing = []

        os.chdir(folder)

        for idx, file in enumerate(xml_files):
            f = os.path.splitext(file)[0]
            name = f'{f}.jpg'
            if os.path.exists(name):
                jpg_files.append(name)
            else:
                images_missing.append(idx)

        for i in images_missing:
            xml_files.pop(i)

        assert len(xml_files) == len(
            jpg_files), "Different number of files present"

        for i, (xml, jpg) in enumerate(zip(xml_files, jpg_files), 1):
            xml_text_check = os.path.splitext(xml)[0]
            jpg_text_check = os.path.splitext(jpg)[0]
            assert xml_text_check == jpg_text_check, "File name doesn't match"
            jpg_filename = f'{folder}_{i}.jpg'
            xml_filename = f'{folder}_{i}.xml'
            os.rename(xml, xml_filename)
            os.rename(jpg, jpg_filename)

        print(f"File renamed in: {folder}")
        os.chdir(root_dir)
    return None


def move_or_copy(src_folders, dest_folder, doCopy=True):
    for folder in src_folders:
        all_files = []
        for root, _, files in itertools.islice(os.walk(folder), 0, None):
            for filename in files:
                all_files.append(os.path.join(root, filename))

        for filename in all_files:
            temp_name = filename.split("\\")[1]
            dest_name = os.path.join(dest_folder, temp_name)
            if doCopy:
                shutil.copy(filename, dest_name)
            else:
                shutil.move(filename, dest_name)
        print("Files Tranfer complete:", folder)
    return None


def find_and_remove_missing(directory):
    jpg_files = sorted(
        [file for file in os.listdir(directory) if file.endswith('.jpg')])

    xml_files = sorted(
        [file for file in os.listdir(directory) if file.endswith('.xml')])

    os.chdir(directory)

    print("JPG - present; XML-missing")
    for i in jpg_files:
        xml = f'{os.path.splitext(i)[0].rstrip()}.xml'
        if not os.path.exists(xml):
            os.remove(i)
            print("Missing and Removed:", xml)

    print("XML - present; JPG-missing")
    for i in xml_files:
        jpg = f'{os.path.splitext(i)[0].rstrip()}.jpg'
        if not os.path.exists(jpg):
            os.remove(i)
            print("Missing and Removed:", jpg)


if __name__ == "__main__":
    curr_dir = os.getcwd()
    # print(curr_dir)
    folders = ['Bus', 'Car']

    # rename_files(curr_dir, folders)
    # print("Renaming Complete")
    # ------------------------------------
    src_folder = ['Bus', 'Car', 'speed_sign']

    dst_folder = input("Enter destination folder name: ")

    # if not os.path.isdir(dst_folder):
    #     os.makedirs(dst_folder)
    #     # shutil.rmtree(dst_folder)

    # move_or_copy(src_folder, dst_folder, doCopy=True)
    # print("Transfer Complete")

    # for i in src_folder:
    #     print("Removing redundant directory: ", i)
    #     shutil.rmtree(i)
    # # ------------------------------------------
    find_and_remove_missing(dst_folder)
