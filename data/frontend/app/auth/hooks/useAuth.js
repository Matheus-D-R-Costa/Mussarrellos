import verbs from "../../utils/verbs";

export const useAuth = async () => {
  const error = null;

  const signup = async (AuthSignup) => {
    error = null;
    try {
      await verbs.post("/auth/signup", AuthSignup);
    } catch (err) {
      error = `Erro ao cadastrar usuário: ${err.message}`;
    }
  };
};
