CUDA_VISIBLE_DEVICES=0 awesome-align \
    --output_file=./raw/bul-pes-fine-align.out \
    --model_name_or_path=./models/bul-pes/ \
    --data_file=./input/bul-pes.src-tgt \
    --extraction 'softmax' \
    --batch_size 32
