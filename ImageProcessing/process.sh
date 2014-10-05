#!/bin/bash

input=$1

./ProcessImage $input $input.jpg
tesseract $input.jpg $input -psm 6
./parse $input.txt data_file $input.out.txt
