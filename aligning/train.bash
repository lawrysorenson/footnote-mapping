CUDA_VISIBLE_DEVICES=0 awesome-train \
    --output_dir=/home/lawry/footnote-mapping/aligning/models/bul-pes \
    --model_name_or_path=bert-base-multilingual-cased \
    --extraction 'softmax' \
    --do_train \
    --train_tlm \
    --train_so \
    --train_data_file=/home/lawry/footnote-mapping/aligning/models/training/bul-pes.train \
    --per_gpu_train_batch_size 2 \
    --gradient_accumulation_steps 4 \
    --num_train_epochs 1 \
    --learning_rate 2e-5 \
    --save_steps 4000 \
    --max_steps 20000 \
    --do_eval \
    --eval_data_file=/home/lawry/footnote-mapping/aligning/models/training/bul-pes.test
