// Relies on globally loaded CryptoJS

function encrypt(plain, password) {
    return CryptoJS.AES.encrypt(plain, password).toString();
}