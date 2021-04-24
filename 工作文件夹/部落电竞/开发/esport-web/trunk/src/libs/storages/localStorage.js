var storage = {
    set(key, value) {
        localStorage.setItem(key, JSON.stringify(value));
    },
    get(key) {
        return JSON.parse(localStorage.getItem(key));
    },
    remove(key) {
        localStorage.removeItem(key);
    },
    canWriteToLocalStorage() {
        try {
            window.localStorage.setItem('_canWriteToLocalStorage', '1');
            window.localStorage.removeItem('_canWriteToLocalStorage');
            return true;
        } catch (e) {
            return false;
        }
    }
};
export default storage;
