import React from "react";

const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload && payload.length) {
    const data = payload[0].payload;
    const valorAtual = Number(data.valorAtual) || 0;
    const valorMeta = Number(data.valorMeta) || 0;

    return (
      <div className="bg-white p-4 border border-gray-300 rounded shadow-lg text-sm">
        <p className="font-bold text-lg mb-1">{data.nome || 'Sem Nome'}</p>
        <p>
          <span className="font-medium text-gray-600">Economizado:</span>
          <span className="ml-2 font-semibold text-blue-600">
            R$ {valorAtual.toFixed(2)}
          </span>
        </p>
        <p>
          <span className="font-medium text-gray-600">Meta:</span>
          <span className="ml-2 font-semibold text-red-600">
            R$ {valorMeta.toFixed(2)}
          </span>
        </p>
      </div>
    );
  }

  return null;
};

export default CustomTooltip;
