#!/bin/bash

#SBATCH --time=0-03:00:00   # walltime
#SBATCH --ntasks=1   # number of processor cores (i.e. tasks)
#SBATCH --nodes=1   # number of nodes
#SBATCH --gpus=1
#SBATCH --mem-per-cpu=30192M   # memory per CPU core
#SBATCH -J "aalign"   # job name

# Set the max number of threads to use for programs using OpenMP. Should be <= ppn. Does nothing if the program doesn't use OpenMP.
export OMP_NUM_THREADS=$SLURM_CPUS_ON_NODE

# LOAD MODULES, INSERT CODE, AND RUN YOUR PROGRAMS HERE
source /fslhome/pipoika3/anaconda3/etc/profile.d/conda.sh
conda activate aalign

cd /fslhome/pipoika3/compute/footnote-mapping/aligning

echo *****

CUDA_VISIBLE_DEVICES=0 awesome-align \
    --output_file=./raw/*****-align.out \
    --model_name_or_path=bert-base-multilingual-cased \
    --data_file=./input/*****.src-tgt \
    --extraction 'softmax' \
    --batch_size 32