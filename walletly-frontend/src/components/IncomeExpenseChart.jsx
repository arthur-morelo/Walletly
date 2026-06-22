import React from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  ReferenceLine,
} from "recharts";

const IncomeExpenseChart = ({ dados }) => {
  const chartData = dados || [];

  if (chartData.length === 0) {
    return <div className="text-center p-4 text-gray-500">Nenhum dado disponível para o gráfico.</div>;
  }

  return (
    <ResponsiveContainer width="100%" height="100%">
      <BarChart data={dados}>
        <CartesianGrid strokeDasharray="3 3" />
        <YAxis />
        <Tooltip formatter={(value) => `R$ ${value.toFixed(2)}`} />
        <Legend />
        <ReferenceLine y={0} stroke="#000" />
        <Bar dataKey="receitas" fill="#22C55E" />
        <Bar dataKey="despesas" fill="#EF4444" />
      </BarChart>
    </ResponsiveContainer>
  );
};

export default IncomeExpenseChart;
