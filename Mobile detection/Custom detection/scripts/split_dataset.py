import pandas as pd
import os
from sklearn.model_selection import train_test_split
import argparse


def generate_split(original, output_dir, ratio=None):
    labels = pd.read_csv(original)
    typ = os.path.splitext(original)[0][-3:]
    # shuffle dataframe
    shuffled_df = labels.sample(frac=1, random_state=42)

    train_df, val_df, _, _ = train_test_split(shuffled_df, shuffled_df['class'],
                                              test_size=ratio,
                                              random_state=42)

    train_df.reset_index(drop=True, inplace=True)

    train_df.to_csv((f'{output_dir}/training_{typ}.csv'), index=None)

    val_df.reset_index(drop=True, inplace=True)
    val_df.to_csv(f'{output_dir}/validation_{typ}.csv', index=None)
    print("Files created")


def main():
    parser = argparse.ArgumentParser(
        description="Create Training and Validatin splits")
    parser.add_argument("-i", "--image-csv", required=True, type=str,
                        help="csv file of original images")
    parser.add_argument("-o", "--output-dir", required=True, type=str,
                        help="Name of output directory")
    parser.add_argument("-r", "--val-size", default=0.1, type=float,
                        help="sizeof validation dataset ")

    args = parser.parse_args()
    generate_split(args.image_csv, args.output_dir, args.val_size)


if __name__ == "__main__":
    main()
