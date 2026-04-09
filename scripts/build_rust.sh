#!/bin/bash

source venv/bin/activate

cd rust
pip install maturin
maturin develop --release