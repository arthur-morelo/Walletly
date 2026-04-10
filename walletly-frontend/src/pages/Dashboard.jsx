import React, { useState, useEffect } from "react";
import DailyBalanceChart from "../components/DailyBalanceChart";
import DailyBalanceTable from "../components/DailyBalanceTable";
import IncomeExpenseChart from "../components/IncomeExpenseChart";
import MonthlyResultsTable from "../components/MonthlyResultsTable";
import Header from "../components/Header";
import api from "../services/api";

const Dashboard = () => {
  const [mesSelecionado, setMesSelecionado] = useState("AGO");
  const [dadosGraficoMensal, setDadosGraficoMensal] = useState([]);
  const [saldoAtual, setSaldoAtual] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function carregarDados() {
      try {
        // Busca dados agrupados por mês para os gráficos
        const responseGeral = await api.get("/transacoes/group/month");
        setDadosGraficoMensal(responseGeral.data);
        
        // Exemplo: Somar saldos de todas as contas para o Saldo Atual
        const responseContas = await api.get("/contas");
        const total = responseContas.data.reduce((acc, conta) => acc + conta.saldoAtual, 0);
        setSaldoAtual(total);

        setLoading(false);
      } catch (error) {
        console.error("Erro ao carregar dados do dashboard:", error);
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
        <div className="bg-white p-6 rounded-lg shadow-md mb-8">
          <h2 className="text-xl font-bold mb-2 text-gray-800">
            Saldo Atual: R$ {saldoAtual.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
          </h2>
          <div className="flex flex-col md:flex-row md:items-center">
            <div className="w-full md:w-2/3 h-80">
              <h3 className="text-lg font-semibold mb-4 text-gray-700">
                Evolução Mensal
              </h3>
              <DailyBalanceChart dados={dadosGraficoMensal} />
            </div>
            <div className="w-full md:w-1/3 mt-6 md:mt-0 md:ml-6">
              <h3 className="text-lg font-semibold mb-4 text-gray-700">
                Resumo por Período
              </h3>
              <DailyBalanceTable dados={dadosGraficoMensal} />
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex flex-col md:flex-row md:items-center">
            <div className="w-full md:w-2/3 h-80">
              <h3 className="text-lg font-semibold mb-4 text-gray-700">
                Receitas e Despesas
                <select
                  className="ml-4 p-1 rounded-md border border-gray-300 text-sm"
                  value={mesSelecionado}
                  onChange={(e) => setMesSelecionado(e.target.value)}
                >
                  <option value="JAN">Janeiro</option>
                  <option value="FEV">Fevereiro</option>
                  <option value="MAR">Março</option>
                  <option value="ABR">Abril</option>
                  <option value="MAI">Maio</option>
                  <option value="JUN">Junho</option>
                  <option value="JUL">Julho</option>
                  <option value="AGO">Agosto</option>
                </select>
              </h3>
              <IncomeExpenseChart dados={dadosGraficoMensal} />
            </div>
            <div className="w-full md:w-1/3 mt-6 md:mt-0 md:ml-6">
              <h3 className="text-lg font-semibold mb-4 text-gray-700">
                Demonstração de Resultado
              </h3>
              <MonthlyResultsTable dados={dadosGraficoMensal} />
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;