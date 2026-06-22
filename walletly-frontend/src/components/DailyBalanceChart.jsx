import React from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const DailyBalanceChart = ({ dados }) => {
  const chartData = dados || [];

  if (chartData.length === 0) {
    return <div className="text-center p-4 text-gray-500">Nenhum dado disponível para o gráfico.</div>;
  }

  return (
    <ResponsiveContainer width="100%" height="100%">
      <BarChart data={dados}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip formatter={(value) => `R$ ${value.toFixed(2)}`} />
        <Bar dataKey="saldo" fill="#F97316" radius={[10, 10, 0, 0]} />
      </BarChart>
    </ResponsiveContainer>
  );
};

export default DailyBalanceChart;
