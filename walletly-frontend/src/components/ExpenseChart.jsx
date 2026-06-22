import React from "react";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from "recharts";

const ExpenseChart = ({ dados }) => {
  return (
    <ResponsiveContainer width="100%" height="100%">
      <AreaChart data={dados} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
        <CartesianGrid strokeDasharray="3 3" vertical={false} />
        <XAxis dataKey="mes" />
        <YAxis tickFormatter={(value) => `R$ ${value}`} />
        <Tooltip formatter={(value) => `R$ ${Number(value).toFixed(2)}`} labelStyle={{ color: 'black' }} />
        <Legend />
        <Area type="monotone" dataKey="gastos" name="Gastos/Despesas" stroke="#EF4444" fill="#FCA5A5" />
      </AreaChart>
    </ResponsiveContainer>
  );
};

export default ExpenseChart;
