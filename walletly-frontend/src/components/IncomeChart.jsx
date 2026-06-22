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
} from "recharts";

const IncomeChart = ({ dados }) => {
  const chartData = dados || [];

  if (chartData.length === 0) {
    return <div className="text-center p-4 text-gray-500">Nenhum dado disponível para o gráfico.</div>;
  }

  return (
    <ResponsiveContainer width="100%" height="100%">
      <BarChart data={chartData}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="mes" />
        <YAxis />
        <Tooltip formatter={(value) => `R$ ${value.toFixed(2)}`} />
        <Legend />
        <Bar dataKey="ganhos" fill="#22C55E" name="Ganhos" />
      </BarChart>
    </ResponsiveContainer>
  );
};

export default IncomeChart;
