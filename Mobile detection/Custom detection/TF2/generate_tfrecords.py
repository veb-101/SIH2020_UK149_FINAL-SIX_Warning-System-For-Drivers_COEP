from __future__ import division
from __future__ import print_function
from __future__ import absolute_import

import os
import io
import pandas as pd
import tensorflow as tf
import argparse

from PIL import Image
from tqdm import tqdm
from object_detection.utils import dataset_util
from collections import namedtuple


def __split(df, group):
    data = namedtuple('data', ['filename', 'object'])
    gb = df.groupby(group)
    return [
        data(filename, gb.get_group(x))
        for filename, x in zip(gb.groups.keys(), gb.groups)
    ]


def class_text_to_int(row_label):
    if row_label == classes[1]:  # 'Bus':
        return 1
    elif row_label == classes[2]:  # Car
        return 2
    elif row_label == classes[3]:  # speed_sign
        return 3
    elif row_label == classes[4]:  # Traffic_light
        return 4
    elif row_label == classes[5]:  # Traffic_sign
        return 5
    elif row_label == classes[6]:  # Person
        return 6
    else:
        None


def create_tf_example(group, path):
    with tf.io.gfile.GFile(os.path.join(path, '{}'.format(group.filename)),
                           'rb') as fid:
        encoded_jpg = fid.read()
    encoded_jpg_io = io.BytesIO(encoded_jpg)
    image = Image.open(encoded_jpg_io)
    width, height = image.size

    filename = group.filename.encode('utf8')
    image_format = b'jpg'
    xmins = []
    xmaxs = []
    ymins = []
    ymaxs = []
    classes_text = []
    classes = []

    for index, row in group.object.iterrows():
        if set(['xmin_rel', 'xmax_rel', 'ymin_rel', 'ymax_rel']).issubset(
                set(row.index)):
            xmin = row['xmin_rel']
            xmax = row['xmax_rel']
            ymin = row['ymin_rel']
            ymax = row['ymax_rel']

        elif set(['xmin', 'xmax', 'ymin', 'ymax']).issubset(set(row.index)):
            xmin = row['xmin'] / width
            xmax = row['xmax'] / width
            ymin = row['ymin'] / height
            ymax = row['ymax'] / height

        xmins.append(xmin)
        xmaxs.append(xmax)
        ymins.append(ymin)
        ymaxs.append(ymax)
        classes_text.append(row['class'].encode('utf8'))
        classes.append(class_text_to_int(row['class']))

    tf_example = tf.train.Example(
        features=tf.train.Features(
            feature={
                'image/height':
                dataset_util.int64_feature(height),
                'image/width':
                dataset_util.int64_feature(width),
                'image/filename':
                dataset_util.bytes_feature(filename),
                'image/source_id':
                dataset_util.bytes_feature(filename),
                'image/encoded':
                dataset_util.bytes_feature(encoded_jpg),
                'image/format':
                dataset_util.bytes_feature(image_format),
                'image/object/bbox/xmin':
                dataset_util.float_list_feature(xmins),
                'image/object/bbox/xmax':
                dataset_util.float_list_feature(xmaxs),
                'image/object/bbox/ymin':
                dataset_util.float_list_feature(ymins),
                'image/object/bbox/ymax':
                dataset_util.float_list_feature(ymaxs),
                'image/object/class/text':
                dataset_util.bytes_list_feature(classes_text),
                'image/object/class/label':
                dataset_util.int64_list_feature(classes),
            }))
    return tf_example


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        description='Create a TFRecord file for use with the TensorFlow Object Detection API.',
        formatter_class=argparse.RawDescriptionHelpFormatter)

    parser.add_argument(
        '--csv-input',
        metavar='csv_input',
        type=str,
        help='Path to the CSV input')

    parser.add_argument(
        '--image-dir',
        metavar='image_dir',
        type=str,
        help='Path to the directory containing all images')

    parser.add_argument(
        '--output-path',
        metavar='output_path',
        type=str,
        help='Path to output TFRecord')

    args = parser.parse_args()

    classes = {
        1: 'Bus',
        2: 'Car',
        3: 'speed_sign',
        4: 'Traffic_light',
        5: 'Traffic_sign',
        6: 'Person'
    }

    writer = tf.io.TFRecordWriter(args.output_path)
    path = os.path.join(args.image_dir)
    examples = pd.read_csv(args.csv_input)
    grouped = __split(examples, 'filename')

    for group in tqdm(grouped, desc='groups'):
        tf_example = create_tf_example(group, path)
        writer.write(tf_example.SerializeToString())

    writer.close()
    output_path = os.path.join(os.getcwd(), args.output_path)
    print('Successfully created the TFRecords: {}'.format(output_path))
