import { useState } from "react";
import { useNavigate, Link } from "react-router-dom"; // 1. Importe useNavigate e Link
import Logo from "../assets/logo.jpg";
import { Input } from "antd";

function Cadastro() {
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");

  const navigate = useNavigate(); // 2. Inicialize o hook

  const handleSubmit = (event) => {
    event.preventDefault();

    if (password !== confirmPassword) {
      setMessage("As senhas não coincidem!");
      return;
    }

    console.log({ nome, email, password });
    setMessage("Cadastro realizado com sucesso!");

    // 3. Redireciona para o login após 2 segundos
    setTimeout(() => {
      navigate("/"); // Leva para a rota da página de login
    }, 2000); // 2000 milissegundos = 2 segundos
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
          {/* Campo Nome */}
          <Input
            type="text"
            placeholder="Nome Completo"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            className="h-12 w-full px-4 text-lg rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />

          {/* Campo E-mail */}
          <Input
            type="email"
            placeholder="E-mail"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="mt-1 h-12 w-full px-4 text-lg rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          {/* Campo Senha */}
          <Input.Password
            placeholder="Senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="h-12 w-full px-4 text-lg rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />

          {/* Campo Confirmar Senha */}
          <Input.Password
            placeholder="Confirmar Senha"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className="h-12 w-full px-4 text-lg rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />

          {message && (
            <p
              className={`text-center text-sm font-semibold ${
                message.includes("sucesso") ? "text-green-300" : "text-red-300"
              }`}
            >
              {message}
            </p>
          )}

          <button
            type="submit"
            className="h-12 w-full bg-white text-gray-500 font-bold text-lg rounded-md transition-transform transform hover:scale-105"
          >
            Cadastrar
          </button>

          {/* 4. Corrigido <a> para <Link> */}
          <div className="text-center text-sm text-white">
            <Link to="/login" className="hover:underline">
              Já tem uma conta? Faça login
            </Link>
          </div>
        </div>
      </form>
    </div>
  );
}

export default Cadastro;
