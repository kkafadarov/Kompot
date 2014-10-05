#!/bin/bash

input=$1
mv "${input}" "${input}.jpg"
ProcessImage "${input}.jpg" "${input}_proc.jpg"
tesseract "${input}_proc.jpg" "${input}" -psm 6
parse "${input}.txt" "${input}.check.txt" "${input}.out.txt"
