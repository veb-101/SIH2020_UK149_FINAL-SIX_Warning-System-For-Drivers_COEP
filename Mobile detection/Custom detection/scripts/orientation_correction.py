'''
    Usage: python scripts/orientation_correction.py -i <image_dir>
    Example: python scripts/orientation_correction.py -i all_images

    References:
        1. https://medium.com/@ageitgey/the-dumb-reason-your-fancy-computer-vision-app-isnt-working-exif-orientation-73166c7d39da
        2. https://www.thepythoncode.com/article/extracting-image-metadata-in-python
        3. https://sirv.com/help/articles/rotate-photos-to-be-upright/#:~:text=The%208%20EXIF%20orientation%20values,degrees%3A%20image%20is%20upside%20down.
'''

import os
import argparse
from PIL import Image
from PIL import ExifTags
import re


def correction(image_dir):

    pat = re.compile('.+\.[jpg, XML, xml, jpeg, png]+$')
    faulty_images = []

    for i, image in enumerate(os.listdir(image_dir), 1):
        if pat.match(image):
            filepath = os.path.join(image_dir, image)
            try:
                img = Image.open(filepath)

                exif_orientation_tag = 274

                # Check for EXIF data (only present on some files)
                if hasattr(img, "_getexif") and isinstance(img._getexif(), dict) and exif_orientation_tag in img._getexif():
                    exif_data = img._getexif()
                    orientation = exif_data[exif_orientation_tag]

                    # Handle EXIF Orientation
                    if orientation == 1:
                        # Normal image - nothing to do!
                        img.close()
                        continue
                    elif orientation == 2:
                        # Mirrored left to right
                        img = img.transpose(PIL.Image.FLIP_LEFT_RIGHT)
                    elif orientation == 3:
                        # Rotated 180 degrees
                        img = img.rotate(180)
                    elif orientation == 4:
                        # Mirrored top to bottom
                        img = img.rotate(180).transpose(
                            PIL.Image.FLIP_LEFT_RIGHT)
                    elif orientation == 5:
                        # Mirrored along top-left diagonal
                        img = img.rotate(-90,
                                         expand=True).transpose(PIL.Image.FLIP_LEFT_RIGHT)
                    elif orientation == 6:
                        # Rotated 90 degrees
                        img = img.rotate(-90, expand=True)
                    elif orientation == 7:
                        # Mirrored along top-right diagonal
                        img = img.rotate(90, expand=True).transpose(
                            PIL.Image.FLIP_LEFT_RIGHT)
                    elif orientation == 8:
                        # Rotated 270 degrees
                        img = img.rotate(90, expand=True)

                    img.save(filepath)
                img.close()

            except (AttributeError, KeyError, IndexError) as e:
                # cases: image don't have getexif
                print(f"Image: {im}, Error: {e}")
                faulty_images.append(filepath)
            except Exception as e:
                faulty_images.append(filepath)

            print(f"\rProcessed: {i} images.", end="")

    if len(faulty_images) > 0:
        print("\nFaulty Images: ")
        for i in faulty_images:
            print(i)


def main():
    parser = argparse.ArgumentParser(description="Correct Image Orientations.")
    parser.add_argument("-i", "--image_dir", help="Image Directory")
    args = parser.parse_args()

    correction(args.image_dir)


if __name__ == "__main__":
    main()
