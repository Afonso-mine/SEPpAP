import numpy as np
import rasterio
from rusle_rust import compute_rusle

class RUSLERasterModel:
    def __init__(self, r, k, ls, c, p):
        self.paths = {"R": r, "K": k, "LS": ls, "C": c, "P": p}

    def read_raster(self, path):
        with rasterio.open(path) as src:
            return src.read(1).astype("float32"), src.profile, src.nodata

    def compute(self, output_path):
        rasters = {}
        nodatas = {}
        profile = None
        shape = None

        for key, path in self.paths.items():
            data, prof, nodata = self.read_raster(path)

            if shape is None:
                shape = data.shape
                profile = prof
            elif data.shape != shape:
                raise ValueError("Rasters must match")

            rasters[key] = data
            nodatas[key] = nodata

        mask = np.zeros(shape, dtype=bool)
        for k in rasters:
            if nodatas[k] is not None:
                mask |= rasters[k] == nodatas[k]

        A = compute_rusle(
            rasters["R"],
            rasters["K"],
            rasters["LS"],
            rasters["C"],
            rasters["P"]
        )

        nodata_val = profile.get("nodata", -9999)
        A = np.where(mask, nodata_val, A)

        profile.update(dtype=rasterio.float32, count=1)

        with rasterio.open(output_path, "w", **profile) as dst:
            dst.write(A.astype("float32"), 1)

        return output_path