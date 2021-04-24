var storage = {
    set(key, value) {
        sessionStorage.setItem(key, JSON.stringify(value));
    },
    get(key) {
        return JSON.parse(sessionStorage.getItem(key));
    },
    remove(key) {
        sessionStorage.removeItem(key)
    },
    canWriteToSessionStorage() {
        try {
            window.sessionStorage.setItem('_canWriteSessionStorage', "1");
            window.sessionStorage.removeItem('_canWriteSessionStorage');
            return true
        } catch (e) {
            return false
        }
    }
}
export default storage;