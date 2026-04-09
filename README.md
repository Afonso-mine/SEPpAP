1. Install javafx, java, rust, python3 and requirements.txt

Linux (DEBIAN BASED): ´sudo apt update && sudo apt install -y openjdk-17-jdk openjfx python3 python3-pip curl build-essential && curl https://sh.rustup.rs -sSf | sh -s -- -y && source $HOME/.cargo/env´

macOS: ´brew update && brew install openjdk openjfx python python3 rust´

2. Compile the code

On both OS (go to SEPpAP/ and run): ´bash scripts/setup_env.sh && bash scripts/build_rust.sh´

3. Run the app

On both OS (go to SEPpAP/ and run): ´bash scripts/run_java.sh´