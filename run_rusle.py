import sys
from python.rusle_core import RUSLERasterModel

def main():
    if len(sys.argv) != 7:
        print("Usage: run_rusle.py R K LS C P OUTPUT")
        sys.exit(1)

    r, k, ls, c, p, out = sys.argv[1:]

    model = RUSLERasterModel(r, k, ls, c, p)
    result = model.compute(out)

    print(f"Saved to: {result}")

if __name__ == "__main__":
    main()