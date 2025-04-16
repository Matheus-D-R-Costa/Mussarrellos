class AuthSignup {
    constructor(name, email, password) {
        this.name = "";
        this.email = "";
        this.password = "";
    }
}

class AuthSignin {
    constructor(email, password) {
        this.email = "";
        this.password = "";
    }
}

export { AuthSignup, AuthSignin };
