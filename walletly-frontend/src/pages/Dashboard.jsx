import React, { useState, useEffect } from "react";
import IncomeChart from "../components/IncomeChart";
import ExpenseChart from "../components/ExpenseChart";
import MonthlyResultsTable from "../components/MonthlyResultsTable";
import Header from "../components/Header";
import api from "../services/api";

const Dashboard = () => {
  const [mesSelecionado, setMesSelecionado] = useState("AGO");
  const [dadosGraficoMensal, setDadosGraficoMensal] = useState([]);
  const [saldoAtual, setSaldoAtual] = useState(0);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState(null);

  useEffect(() => {
    async function carregarDados() {
      try {
        // Busca dados agrupados por mês para os gráficos
        const responseGeral = await api.get("/dashboard/resumo-mensal");
        console.log("Resumo Mensal:", responseGeral.data);
        setDadosGraficoMensal(responseGeral.data);
        setErrorMsg(null);

        // Exemplo: Somar saldos de todas as contas para o Saldo Atual
        const responseContas = await api.get("/contas");
        const contas = Array.isArray(responseContas.data) ? responseContas.data : responseContas.data.content || [];
        const total = contas.reduce((acc, conta) => acc + conta.saldoAtual, 0);
        setSaldoAtual(total);

        setLoading(false);
      } catch (error) {
        console.error("Erro ao carregar dados do dashboard:", error);
        setErrorMsg(error.response ? "Erro API: " + error.response.status + " " + JSON.stringify(error.response.data) : "Erro de conexão: " + error.message);
        setLoading(false);
      }
    }
    carregarDados();
  }, []);

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Carregando dados...</div>;
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <Header />

      <main className="container mx-auto p-8">
        <div className="bg-white p-6 rounded-lg shadow-sm mb-6">
          <h2 className="text-xl font-bold text-gray-800 mb-2">
            Saldo Atual: R$ {saldoAtual.toFixed(2)}
          </h2>
          {errorMsg && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
              <strong className="font-bold">Ops! </strong>
              <span className="block sm:inline">{errorMsg}</span>
            </div>
          )}
          {(!dadosGraficoMensal || dadosGraficoMensal.length === 0) && !errorMsg && !loading && (
            <div className="bg-yellow-50 border border-yellow-200 text-yellow-800 px-4 py-3 rounded relative mb-4">
              Nenhuma transação encontrada. Você já fez o upload de um arquivo OFX no menu "Extratos"?
            </div>
          )}
          <div className="flex flex-col md:flex-row md:items-center">
            <div className="w-full md:w-2/3 h-80">
              <h3 className="text-lg font-semibold mb-4 text-gray-700">
                Ganhos por Mês (Receitas)
              </h3>
              <IncomeChart dados={dadosGraficoMensal} />
            </div>
            <div className="w-full md:w-1/3 mt-6 md:mt-0 md:ml-6">
              <br />
              <h3 className="text-lg font-semibold mb-4 text-gray-700">
                Tabela de Ganhos
              </h3>
              <div className="w-full bg-gray-100 rounded-lg overflow-hidden max-h-80 overflow-y-auto">
                <table className="min-w-full text-center divide-y divide-gray-300">
                  <thead className="bg-gray-200">
                    <tr><th className="py-2 px-4 text-sm font-semibold text-gray-600">Ganhos do Mês</th></tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {dadosGraficoMensal.map((item, index) => (
                      <tr key={index}>
                        <td className="py-2 text-sm text-gray-800">
                          {item.mes} - R$ {item.ganhos.toFixed(2)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex flex-col md:flex-row md:items-center">
            <div className="w-full md:w-2/3 h-80">
              <h3 className="text-lg font-semibold mb-4 text-gray-700">
                Despesas por Mês (Gastos)
              </h3>
              <ExpenseChart dados={dadosGraficoMensal} />
            </div>
            <div className="w-full md:w-1/3 mt-6 md:mt-0 md:ml-6">
              <h3 className="text-lg font-semibold mb-4 text-gray-700">
                Tabela de Gastos
              </h3>
              <div className="w-full bg-gray-100 rounded-lg overflow-hidden max-h-80 overflow-y-auto">
                <table className="min-w-full text-center divide-y divide-gray-300">
                  <thead className="bg-gray-200">
                    <tr><th className="py-2 px-4 text-sm font-semibold text-gray-600">Gastos do Mês</th></tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {dadosGraficoMensal.map((item, index) => (
                      <tr key={index}>
                        <td className="py-2 text-sm text-gray-800">
                          {item.mes} - R$ {item.gastos.toFixed(2)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;