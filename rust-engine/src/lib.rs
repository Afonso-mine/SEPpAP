#[no_mangle]
pub extern "C" fn calculate_erosion(rain: f64, k: f64, ls: f64, c: f64, p: f64) -> f64 {
    let r = 0.5 * rain;
    r * k * ls * c * p
}