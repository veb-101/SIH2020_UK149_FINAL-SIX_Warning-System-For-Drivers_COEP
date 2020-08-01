import os
import shutil
import itertools
import os.path as P


def move_or_copy_IDD_images(from_folders, dest_folder, doCopy=True):
    i = 0
    for folder in from_folders:
        all_files = []
        for root, _, files in itertools.islice(os.walk(folder), 1, None):
            for filename in files:
                all_files.append(os.path.join(root, filename))

        for filename in all_files:
            dest_name = os.path.join(dest_folder, f'{i}.jpg')
            print(f"\r{i} files transfered.", end="")
            if doCopy:
                shutil.copy(filename, dest_name)
            else:
                shutil.move(filename, dest_name)
            i += 1


def move_or_copy_IDD_XML(folder, dest_folder, doCopy=True):
    i = 1
    xml_files = [P.join(folder, file)
                 for file in os.listdir(folder) if file.endswith('.xml')]

    for filename in xml_files:
        dest_name = os.path.join(dest_folder, filename.split('\\')[1])
        print(f"\r{i} files transfered.", end="")
        if doCopy:
            shutil.copy(filename, dest_name)
        else:
            shutil.move(filename, dest_name)
        i += 1


def delete_other_IDD_images(root_dir):
    jpg_files = sorted(
        [P.join(root_dir, file) for file in os.listdir(root_dir) if file.endswith('.jpg')])

    for idx, file in enumerate(jpg_files, 1):
        f = P.splitext(file)[0]
        name = f'{f}.xml'
        if not P.exists(name):
            os.remove(file)
        print(f"\r{idx} images processed.", end='')
    print()
    return None


def rename_IDD_data(folder):
    xml_files = sorted(
        [os.path.join(folder, file) for file in os.listdir(folder) if file.endswith('.xml')])
    print(len(xml_files))
    jpg_files = sorted(
        [os.path.join(folder, file) for file in os.listdir(folder) if file.endswith('.jpg')])
    print(len(jpg_files))

    jpg_files = []
    images_missing = []

    for idx, file in enumerate(xml_files):
        fname, _ = os.path.splitext(file)
        name = f'{fname}.jpg'
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
        jpg_filename = os.path.join(folder, f'IDD_{i}.jpg')
        xml_filename = os.path.join(folder, f'IDD_{i}.xml')
        os.rename(xml, xml_filename)
        os.rename(jpg, jpg_filename)


def move_to_dataset(src_folder, dest_folder, doCopy=True):
    for i, file in enumerate(os.listdir(src_folder), 1):
        src_path = P.join(src_folder, file)
        dest_path = P.join(dest_folder, file)
        print(f"\r{i} files transfered.", end="")
        if doCopy:
            shutil.copy(src_path, dest_path)
        else:
            shutil.move(src_path, dest_path)


if __name__ == '__main__':
    src_folder = [
        r'C:\Users\batman\Downloads\idd-detection\IDD_Detection\JPEGImages\frontFar',
        r'C:\Users\batman\Downloads\idd-detection\IDD_Detection\JPEGImages\frontNear'
    ]

    dst_folder = input("Enter destination folder name: ")
    if not os.path.isdir(dst_folder):
        os.makedirs(dst_folder)

    move_or_copy_IDD_images(src_folder, dst_folder, doCopy=True)
    print("\nImage copy Complete")
    move_or_copy_IDD_XML('IDD_Image_XML', dst_folder)
    print("\nXML copy Complete")
    delete_other_IDD_images('IDD_DATA')
    print("\nRedundant images removed")
    rename_IDD_data('IDD_DATA')
    print("\nRenamed data")
    move_to_dataset('IDD_DATA', 'all_images', doCopy=True)
    print("IDD data moved with main dataset")
