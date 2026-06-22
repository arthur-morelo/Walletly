import React, { useState, useEffect } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  ReferenceLine,
} from "recharts";
import CustomTooltip from "../components/CustomTooltip.jsx";
import Header from "../components/Header";
import api from "../services/api";

const GoalTracker = () => {
  const [goals, setGoals] = useState([]);
  const [newGoal, setNewGoal] = useState({ name: "", goalValue: 0, savedValue: 0 });

  const [selectedGoalId, setSelectedGoalId] = useState("");
  const [amountToAdd, setAmountToAdd] = useState(0);

  useEffect(() => {
    fetchGoals();
  }, []);

  const fetchGoals = async () => {
    try {
      const response = await api.get("/metas");
      setGoals(response.data);
      if (response.data.length > 0) {
        setSelectedGoalId(response.data[0].id);
      } else {
        setSelectedGoalId("");
      }
    } catch (error) {
      console.error("Erro ao buscar metas:", error);
    }
  };

  const handleAddGoal = async (e) => {
    e.preventDefault();
    if (!newGoal.name || parseFloat(newGoal.goalValue) <= 0) return;

    try {
      const response = await api.post("/metas", {
        name: newGoal.name,
        goalValue: parseFloat(newGoal.goalValue),
        savedValue: parseFloat(newGoal.savedValue) || 0,
      });
      setGoals([...goals, response.data]);
      setNewGoal({ name: "", goalValue: 0, savedValue: 0 });
      if (!selectedGoalId) {
        setSelectedGoalId(response.data.id);
      }
    } catch (error) {
      console.error("Erro ao adicionar meta:", error);
    }
  };

  const handleUpdateGoal = async (e) => {
    e.preventDefault();
    if (amountToAdd <= 0 || !selectedGoalId) return;

    const goalToUpdate = goals.find((g) => g.id === parseInt(selectedGoalId));
    if (!goalToUpdate) return;

    const newSavedValue = goalToUpdate.savedValue + parseFloat(amountToAdd);

    try {
      const response = await api.put(`/metas/${selectedGoalId}`, {
        name: goalToUpdate.name,
        goalValue: goalToUpdate.goalValue,
        savedValue: newSavedValue,
      });

      // Verificar se bateu a meta
      if (newSavedValue >= goalToUpdate.goalValue) {
        alert("Parabéns! Você atingiu a sua meta financeira com sucesso!");
        await api.delete(`/metas/${selectedGoalId}`);
        fetchGoals(); // Recarrega para remover a meta
      } else {
        setGoals(
          goals.map((g) => (g.id === parseInt(selectedGoalId) ? response.data : g))
        );
      }
      setAmountToAdd(0);
    } catch (error) {
      console.error("Erro ao atualizar meta:", error);
    }
  };

  const handleRemoveGoal = async (goalIdToRemove) => {
    try {
      await api.delete(`/metas/${goalIdToRemove}`);
      setGoals(goals.filter((goal) => goal.id !== goalIdToRemove));
      if (parseInt(selectedGoalId) === goalIdToRemove) {
        setSelectedGoalId("");
      }
    } catch (error) {
      console.error("Erro ao remover meta:", error);
    }
  };

  return (
    <div className="bg-gray-100 mx-auto">
      <Header />
      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <h2 className="text-xl font-bold mb-4">Adicionar Nova Meta</h2>
        <form
          onSubmit={handleAddGoal}
          className="flex flex-col space-y-4 md:flex-row md:space-x-4 md:space-y-0"
        >
          <input
            type="text"
            placeholder="Nome da Meta"
            value={newGoal.name}
            onChange={(e) => setNewGoal({ ...newGoal, name: e.target.value })}
            className="flex-1 p-2 border border-gray-300 rounded-md"
          />
          <input
            type="number"
            placeholder="Valor da Meta (R$)"
            value={newGoal.goalValue || ""}
            onChange={(e) => setNewGoal({ ...newGoal, goalValue: e.target.value })}
            className="flex-1 p-2 border border-gray-300 rounded-md"
          />
          <input
            type="number"
            placeholder="Economizado (R$)"
            value={newGoal.savedValue || ""}
            onChange={(e) => setNewGoal({ ...newGoal, savedValue: e.target.value })}
            className="flex-1 p-2 border border-gray-300 rounded-md"
          />
          <button
            type="submit"
            className="bg-indigo-600 text-white p-2 rounded-md hover:bg-indigo-700"
          >
            Adicionar Meta
          </button>
        </form>
      </div>

      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <h2 className="text-xl font-bold mb-4">
          Adicionar Valor a uma Meta Existente
        </h2>
        <form
          onSubmit={handleUpdateGoal}
          className="flex flex-col space-y-4 md:flex-row md:space-x-4 md:space-y-0"
        >
          <select
            value={selectedGoalId || ""}
            onChange={(e) => setSelectedGoalId(e.target.value)}
            className="flex-1 p-2 border border-gray-300 rounded-md"
          >
            <option value="" disabled>Selecione uma meta</option>
            {goals.map((goal) => (
              <option key={goal.id} value={goal.id}>
                {goal.name}
              </option>
            ))}
          </select>
          <input
            type="number"
            placeholder="Valor a Adicionar (R$)"
            value={amountToAdd || ""}
            onChange={(e) => setAmountToAdd(parseFloat(e.target.value))}
            className="flex-1 p-2 border border-gray-300 rounded-md"
          />
          <button
            type="submit"
            className="bg-green-600 text-white p-2 rounded-md hover:bg-green-700"
          >
            Adicionar Valor
          </button>
        </form>
      </div>

      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-xl font-bold mb-4">Progresso das Metas</h2>
        <div className="h-96">
          {goals && goals.length > 0 ? (
            <ResponsiveContainer width="100%" height="100%">
              <BarChart
                data={goals}
                layout="vertical"
                margin={{ top: 20, right: 30, left: 100, bottom: 5 }}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="number" />
                <YAxis type="category" dataKey="name" />

                <Tooltip content={<CustomTooltip />} />

                <Bar dataKey="savedValue" fill="#3B82F6" />

                {goals.map((goal) => (
                  <ReferenceLine
                    key={goal.id}
                    x={goal.goalValue}
                    stroke="#EF4444"
                    strokeDasharray="3 3"
                  />
                ))}
              </BarChart>
            </ResponsiveContainer>
          ) : (
             <p className="text-gray-500 text-center py-10">Nenhuma meta cadastrada.</p>
          )}
        </div>
      </div>

      <div className="bg-white p-6 rounded-lg shadow-md mt-8">
        <h2 className="text-xl font-bold mb-4">Gerenciar Metas</h2>
        <ul className="space-y-4">
          {goals.map((goal) => (
            <li
              key={goal.id}
              className="flex items-center justify-between p-4 border border-gray-200 rounded-md"
            >
              <span>
                {goal.name}: R$ {goal.savedValue.toFixed(2)} / R${" "}
                {goal.goalValue.toFixed(2)}
              </span>
              <button
                onClick={() => handleRemoveGoal(goal.id)}
                className="bg-red-600 text-white px-3 py-1 rounded-md hover:bg-red-700 transition-colors duration-200"
              >
                Remover
              </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default GoalTracker;
