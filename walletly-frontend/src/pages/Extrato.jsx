import React, { useState, useEffect } from "react";
import Logo from "../assets/logo.jpg";
import { getTransacoes } from "../services/transacaoService";

function ExtratosPage() {
  const [transacoes, setTransacoes] = useState([]);

  useEffect(() => {
    carregarTransacoes();
  }, []);

  const carregarTransacoes = async () => {
    try {
      const response = await getTransacoes({});
      setTransacoes(response.data.content || response.data);
    } catch (error) {
      console.error("Erro ao buscar extrato", error);
    }
  };
  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-sm bg-gray-500 rounded-lg shadow-lg overflow-hidden">
        {/* Header com o logo */}
        <img
          src={Logo}
          alt="Logo da Empresa"
          className="w-full h-28 object-cover"
        />

        {/* Conteúdo da página */}
        <div className="p-6 text-center">
          <h2 className="text-2xl font-semibold text-white mb-6">Extratos</h2>

          {/* Caixa de texto superior */}
          <div className="bg-cyan-900 text-white rounded-lg p-4 mb-6 ">
            <p className="text-sm">
              Para criarmos seu controle financeiro basta inserir todos os seus
              extratos bancários do período em que deseja.
            </p>
          </div>

          {/* Botão Adicionar */}
          <a href="/" className="">
            <button className="w-3/4 bg-orange-500 text-white py-3 rounded-lg font-bold text-lg hover:bg-orange-600 transition duration-300">
              Adicionar
            </button>
          </a>

          {/* Caixa de texto inferior */}
          <div className="bg-cyan-900 text-white rounded-lg p-4 mt-6">
            <p className="text-sm">
              O sistema carrega os anexos, distribuindo as transações entre
              despesas e receitas de acordo com os extratos, alimentando as
              telas do aplicativo.
            </p>
          </div>

          {/* Lista de Transações (Exemplo básico) */}
          <div className="mt-6 text-left">
            <h3 className="text-white font-semibold mb-2">Transações Recentes</h3>
            {transacoes && transacoes.length > 0 ? (
              <ul className="bg-white rounded-lg shadow p-4">
                {transacoes.map((t, index) => (
                  <li key={index} className="border-b last:border-0 py-2 text-sm text-gray-700">
                    {t.descricao} - R$ {t.valor}
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-300 text-sm">Nenhuma transação encontrada.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ExtratosPage;
