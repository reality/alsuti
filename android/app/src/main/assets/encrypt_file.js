// Relies on globally loaded CryptoJS

function encrypt(password) {
    return CryptoJS.AES.encrypt(plain, password);
}