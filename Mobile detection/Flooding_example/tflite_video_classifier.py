from tflite_runtime.interpreter import Interpreter, load_delegate
import argparse
import time
import cv2
import re
import numpy as np


def load_labels(path):
    """Loads the labels file. Supports files with or without index numbers."""
    with open(path, "r", encoding="utf-8") as f:
        lines = f.readlines()
        labels = {}
        for row_number, content in enumerate(lines):
            pair = re.split(r"[:\s]+", content.strip(), maxsplit=1)
            if len(pair) == 2 and pair[0].strip().isdigit():
                labels[int(pair[0])] = pair[1].strip()
            else:
                labels[row_number] = pair[0].strip()
    # print(labels)
    return labels


def set_input_tensor(interpreter, image):
    """Sets the input tensor."""
    tensor_index = interpreter.get_input_details()[0]["index"]
    input_tensor = interpreter.tensor(tensor_index)()[0]
    input_tensor[:, :] = image


def get_output_tensor(interpreter, index):
    """Returns the output tensor at the given index."""
    output_details = interpreter.get_output_details()[index]
    tensor = np.squeeze(interpreter.get_tensor(output_details["index"]))
    return tensor


def make_interpreter(model_file, use_edgetpu):
    model_file, *device = model_file.split("@")
    if use_edgetpu:
        return Interpreter(
            model_path=model_file,
            experimental_delegates=[
                load_delegate(
                    "libedgetpu.so.1", {"device": device[0]} if device else {}
                )
            ],
        )
    else:
        return Interpreter(model_path=model_file)


def runClassifier(interpreter, image, threshold):
    """Returns a list of detection results, each a
    dictionary of object info."""
    set_input_tensor(interpreter, image)
    interpreter.invoke()

    # Get all output details
    boxes = get_output_tensor(interpreter, 0)
    classes = get_output_tensor(interpreter, 1)
    scores = get_output_tensor(interpreter, 2)
    count = int(get_output_tensor(interpreter, 3))

    results = []
    for i in range(count):
        if scores[i] >= threshold:
            result = {
                "bounding_box": boxes[i],
                "class_id": classes[i],
                "score": scores[i],
            }
            results.append(result)
    return results


def main():
    parser = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )
    parser.add_argument(
        "-m", "--model", type=str, required=True,
        help="File path of .tflite file."
    )

    parser.add_argument(
        "-t",
        "--threshold",
        type=float,
        default=0.4,
        required=False,
        help="Score threshold for detected objects.",
    )
    parser.add_argument("-v", "--video", type=str,
                        required=True, help="Path to video")
    parser.add_argument(
        "-e", "--use_edgetpu", action="store_true", default=False, help="Use EdgeTPU"
    )
    args = parser.parse_args()

    # labels = load_labels(args.labels)

    interpreter = Interpreter(model_path=args.model)
    interpreter.allocate_tensors()

    input_details = interpreter.get_input_details()
    input_index = input_details[0]["index"]
    height = input_details[0]["shape"][1]
    width = input_details[0]["shape"][2]

    output_details = interpreter.get_output_details()
    output_index = output_details[0]["index"]
    print(input_index, output_index, height, width)
    # Initialize video stream
    cap = cv2.VideoCapture(args.video)
    time.sleep(1)

    width = int(cap.get(3))  # float
    height = int(cap.get(4))
    fourcc = cv2.VideoWriter_fourcc(*'XVID')
    output = cv2.VideoWriter('output_Test_PC_3.avi',
                             fourcc, 20.0, (width, height))

    while cap.isOpened():
        try:
            ret, frame = cap.read()
            frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            frame_resized = cv2.resize(frame_rgb, (32, 32))
            frame_resized = frame_resized.astype('float32') / 255.0
            input_data = np.expand_dims(frame_resized, axis=0)

            interpreter.set_tensor(input_index, input_data)
            interpreter.invoke()
            output_data = interpreter.get_tensor(output_index)
            if output_data > 0.6:
                output_data = "Flooded"
            else:
                output_data = "Not Flooded"
            print(f"\rDetection: {output_data}", end="")

            cv2.putText(
                frame,
                f"Current Frame: {output_data}",
                (0, 100),
                cv2.FONT_HERSHEY_SIMPLEX,
                1.0,
                (255, 255, 255),
                3,
            )
            cv2.imshow("Flood detection", frame)

            output.write(frame)

            if cv2.waitKey(5) & 0xFF == ord("q"):
                # fps.stop()
                break
        except KeyboardInterrupt:
            break

    cv2.destroyAllWindows()
    cap.release()
    # time.sleep(2)


if __name__ == "__main__":
    main()
