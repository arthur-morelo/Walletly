import React, { useState } from "react";
import DailyBalanceChart from "../components/DailyBalanceChart";
import DailyBalanceTable from "../components/DailyBalanceTable";
import IncomeExpenseChart from "../components/IncomeExpenseChart";
import MonthlyResultsTable from "../components/MonthlyResultsTable";
import Header from "../components/Header";
const Dashboard = () => {
  const [mesSelecionado, setMesSelecionado] = useState("AGO");
  const [dadosDiariosSaldos, setDadosDiariosSaldos] = useState([]);
  const [dadosDiariosTabela, setDadosDiariosTabela] = useState([]);
  const [dadosExtratoMensal, setDadosExtratoMensal] = useState([]);
  const [dadosResultadoMensal, setDadosResultadoMensal] = useState([]);

  const dadosFiltradosMensal = dadosExtratoMensal && dadosExtratoMensal.length > 0
    ? dadosExtratoMensal.filter((item) => item.name === mesSelecionado)
    : [];

  return (
    // 1. Div principal para ocupar a tela inteira e dar uma cor de fundo
    <div className="min-h-screen bg-gray-100 dark:bg-slate-950 transition-colors duration-300">
      {/* O Header agora está aqui fora, ocupando a largura total */}
      <Header />

      {/* 2. Um novo container APENAS para o conteúdo do dashboard */}
      <main className="container mx-auto p-8">
        <div className="bg-white dark:bg-slate-900 p-6 rounded-lg shadow-md mb-8 transition-colors duration-300">
          <h2 className="text-xl font-bold mb-2 text-gray-800 dark:text-slate-100">
            Saldo Atual: R$ 0,00
          </h2>
          <div className="flex flex-col">
            <div className="w-full h-80 mb-8">
              <h3 className="text-lg font-semibold mb-4 text-gray-700 dark:text-slate-200">
                Saldo de {mesSelecionado}
              </h3>
              <DailyBalanceChart dados={dadosDiariosSaldos} />
            </div>
            <div className="w-full mt-6">
              <h3 className="text-lg font-semibold mb-4 text-gray-700 dark:text-slate-200">
                Saldos em {mesSelecionado}
              </h3>
              <DailyBalanceTable dados={dadosDiariosTabela} />
            </div>
          </div>
        </div>

        <div className="bg-white dark:bg-slate-900 p-6 rounded-lg shadow-md mb-8 transition-colors duration-300">
          <div className="flex flex-col">
            <div className="w-full h-80 mb-8">
              <h3 className="text-lg font-semibold mb-4 text-gray-700 dark:text-slate-200">
                Receitas e Despesas
                <select
                  className="ml-4 p-1 rounded-md border border-gray-300 dark:border-slate-700 dark:bg-slate-800 dark:text-slate-200 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                  value={mesSelecionado}
                  onChange={(e) => setMesSelecionado(e.target.value)}
                >
                  {dadosExtratoMensal.map((item) => (
                    <option key={item.name} value={item.name}>
                      {item.name}
                    </option>
                  ))}
                </select>
              </h3>
              <IncomeExpenseChart dados={dadosFiltradosMensal} />
            </div>
            <div className="w-full mt-6">
              <h3 className="text-lg font-semibold mb-4 text-gray-700 dark:text-slate-200">
                Demonstração de Resultado
              </h3>
              <MonthlyResultsTable dados={dadosResultadoMensal} />
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
