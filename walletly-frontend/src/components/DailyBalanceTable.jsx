import React from "react";

const DailyBalanceTable = ({ dados }) => {
  return (
    <div className="w-full bg-gray-100 rounded-lg overflow-hidden">
      <table className="min-w-full text-center divide-y divide-gray-300">
        <thead className="bg-gray-200">
          <tr>
            <th className="py-2 px-4 text-sm font-semibold text-gray-600">
              Saldos do Mês
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {(!dados || dados.length === 0) ? (
            <tr>
              <td className="py-2 text-sm text-gray-500">Nenhum dado disponível.</td>
            </tr>
          ) : dados.map((item, index) => (
            <tr key={index}>
              <td className="py-2 text-sm text-gray-800">
                {item.data} - R$ {item.saldo.toFixed(2)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DailyBalanceTable;
