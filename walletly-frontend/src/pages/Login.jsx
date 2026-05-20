import { useState } from "react";
import { useNavigate, Link } from "react-router-dom"; // 1. Importe useNavigate e Link
import Logo from "../assets/logo.jpg";
import { Input } from "antd";
import { useAuth } from "../contexts/AuthContext";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const { signIn } = useAuth();
  const navigate = useNavigate(); // 2. Inicialize o hook

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await signIn(email, password);
      // Se o backend retornar sucesso e o cookie JWT for salvo:
      console.log("Login realizado, redirecionando...");
      navigate("/dashboard");
    } catch (error) {
      alert("Erro ao fazer login. Verifique suas credenciais.");
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <form
        onSubmit={handleSubmit}
        className="flex flex-col bg-gray-500 w-full max-w-sm rounded-xl shadow-lg overflow-hidden"
      >
        <img
          src={Logo}
          alt="Logo da Empresa"
          className="w-full h-28 object-cover"
        />

        <div className="flex flex-col p-8 gap-y-6">
          <Input
            id="email"
            type="email"
            placeholder="E-mail"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="mt-1 h-12 w-full px-4 text-lg rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          <Input.Password
            placeholder="Senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="h-12 w-full px-4 text-lg rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />

          <button
            type="submit"
            className="h-12 w-full bg-white text-gray-500 font-bold text-lg rounded-md transition-transform transform hover:scale-105"
          >
            Login
          </button>

          <div className="flex justify-between w-full text-sm text-white">
            <Link to="/cadastro" className="hover:underline">
              Criar conta
            </Link>
            <Link to="/EsqueceuSenha" className="hover:underline">
              Esqueceu a senha?
            </Link>
          </div>
        </div>
      </form>
    </div>
  );
}

export default Login;
