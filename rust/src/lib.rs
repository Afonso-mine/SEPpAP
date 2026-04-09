use numpy::{PyArray2, PyReadonlyArray2};
use pyo3::prelude::*;

#[pyfunction]
fn compute_rusle(
    r: PyReadonlyArray2<f32>,
    k: PyReadonlyArray2<f32>,
    ls: PyReadonlyArray2<f32>,
    c: PyReadonlyArray2<f32>,
    p: PyReadonlyArray2<f32>,
) -> Py<PyArray2<f32>> {

    let r = r.as_array();
    let k = k.as_array();
    let ls = ls.as_array();
    let c = c.as_array();
    let p = p.as_array();

    let result = &r * &k * &ls * &c * &p;

    Python::with_gil(|py| PyArray2::from_array(py, &result).to_owned())
}

#[pymodule]
fn rusle_rust(_py: Python, m: &PyModule) -> PyResult<()> {
    m.add_function(wrap_pyfunction!(compute_rusle, m)?)?;
    Ok(())
}