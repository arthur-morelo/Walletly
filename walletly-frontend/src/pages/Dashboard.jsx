import React, { useState, useEffect } from "react";
import DailyBalanceChart from "../components/DailyBalanceChart";
import DailyBalanceTable from "../components/DailyBalanceTable";
import IncomeExpenseChart from "../components/IncomeExpenseChart";
import MonthlyResultsTable from "../components/MonthlyResultsTable";
import Header from "../components/Header";
import api from "../services/api";

const Dashboard = () => {
  const [mesSelecionado, setMesSelecionado] = useState("AGO");
  const [hasData, setHasData] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function checkData() {
      try {
        // Verifica se existe algum arquivo enviado no extrato
        const response = await api.get("/extratos/history");
        const extratos = response.data || [];
        
        if (extratos.length > 0) {
          setHasData(true);
        } else {
          setHasData(false);
        }
      } catch (error) {
        console.error("Erro ao checar extratos:", error);
        setHasData(false);
      } finally {
        setLoading(false);
      }
    }
    checkData();
  }, []);

  // Dados Mockados Fixos para garantir a exibição no painel
  const dadosExtratoMensalMock = [
    { name: "JUN", mes: "JUN", receitas: 12000.0, despesas: 6000.0, valor: 6000.0 },
    { name: "JUL", mes: "JUL", receitas: 13500.0, despesas: 7200.0, valor: 6300.0 },
    { name: "AGO", mes: "AGO", receitas: 15256.0, despesas: 8000.0, valor: 7256.0 },
    { name: "SET", mes: "SET", receitas: 16500.0, despesas: 9200.0, valor: 7300.0 }
  ];

  const dadosDiariosMock = [
    { name: "1", data: "01/AGO", saldo: 10530.0 },
    { name: "5", data: "05/AGO", saldo: 12829.0 },
    { name: "10", data: "10/AGO", saldo: 15256.0 },
    { name: "15", data: "15/AGO", saldo: 14000.0 },
    { name: "20", data: "20/AGO", saldo: 12500.0 },
    { name: "25", data: "25/AGO", saldo: 9500.0 },
    { name: "30", data: "30/AGO", saldo: 7256.0 }
  ];

  const saldoAtualMock = 15256.00;

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Carregando dados...</div>;
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <Header />
      <main className="container mx-auto p-8">
        {!hasData ? (
          <div className="bg-white p-6 rounded-lg shadow-md mb-8">
            <div className="bg-yellow-50 border border-yellow-200 text-yellow-800 px-4 py-3 rounded relative">
              Nenhuma transação encontrada. Você já fez o upload de um arquivo OFX no menu "Extratos"?
            </div>
          </div>
        ) : (
          <>
            <div className="bg-white p-6 rounded-lg shadow-md mb-8">
              <div className="flex justify-between items-center mb-6">
                <h2 className="text-xl font-bold text-gray-800">
                  Saldo Atual: R$ {saldoAtualMock.toFixed(2)}
                </h2>
              </div>

              <div className="flex flex-col md:flex-row md:items-center">
                <div className="w-full md:w-2/3 h-80">
                  <h3 className="text-lg font-semibold mb-4 text-gray-700">
                    Saldo Diário de {mesSelecionado}
                  </h3>
                  <DailyBalanceChart dados={dadosDiariosMock} />
                </div>
                <div className="w-full md:w-1/3 mt-6 md:mt-0 md:ml-6">
                  <h3 className="text-lg font-semibold mb-4 text-gray-700">
                    Saldos Diários em {mesSelecionado}
                  </h3>
                  <DailyBalanceTable dados={dadosDiariosMock} />
                </div>
              </div>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-md">
              <div className="flex flex-col md:flex-row md:items-center">
                <div className="w-full md:w-2/3 h-80">
                  <h3 className="text-lg font-semibold mb-4 text-gray-700">
                    Receitas e Despesas
                    <select
                      className="ml-4 p-1 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
                      value={mesSelecionado}
                      onChange={(e) => setMesSelecionado(e.target.value)}
                    >
                      {dadosExtratoMensalMock.map((item) => (
                        <option key={item.name} value={item.name}>
                          {item.name}
                        </option>
                      ))}
                    </select>
                  </h3>
                  <IncomeExpenseChart dados={dadosExtratoMensalMock} />
                </div>
                <br />
                <div className="w-full md:w-1/3 mt-6 md:mt-0 md:ml-6">
                  <h3 className="text-lg font-semibold mb-4 text-gray-700">
                    Demonstração de Resultado
                  </h3>
                  <MonthlyResultsTable dados={dadosExtratoMensalMock} />
                </div>
              </div>
            </div>
          </>
        )}
      </main>
    </div>
  );
};

export default Dashboard;