#!/bin/bash
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
echo $SCRIPT_DIR
printf '\e[38;2;0;119;182m\e[48;2;8;8;8mHello World!\e[0m\n'
#          fg   RRR GGG BBB    bg   RRR GGG BB             reset
