CUDA_VISIBLE_DEVICES=0 awesome-align \
    --output_file=bul-pes-align.out \
    --model_name_or_path=bert-base-multilingual-cased \
    --data_file=bul-pes.src-tgt \
    --extraction 'softmax' \
    --batch_size 32