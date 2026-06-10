import {
  Route,
  createBrowserRouter,
  createRoutesFromElements,
} from "react-router-dom";

import Login from "../pages/Login";
import Cadastro from "../pages/Cadastro";
import Start from "../pages/Start";
import Extrato from "../pages/Extrato";
import Dashboard from "../pages/Dashboard";
import Metas from "../pages/GoalChart";
import EsqueceuSenha from "../pages/EsqueceuSenha";
import Perfil from '../pages/Perfil';
import CursosList from '../pages/Cursos/CursosList';
import Curso from "../pages/Cursos/CursoPage";
import Planos from "../pages/Planos";
import ProtectedRoute from "../components/ProtectedRoute";
import PlanRoute from "../components/PlanRoute";

// 2. Crie e exporte o roteador
const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/">
      <Route index element={<Start />} />

      <Route path="perfil" element={<ProtectedRoute><Perfil /></ProtectedRoute>} />

      <Route path="cadastro" element={<Cadastro />} />

      <Route path="login" element={<Login />} />

      <Route path="extrato" element={<ProtectedRoute><Extrato /></ProtectedRoute>} />

      <Route path="dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />

      <Route path="metas" element={<ProtectedRoute><Metas /></ProtectedRoute>} />

      <Route path="cursos" element={<ProtectedRoute><PlanRoute><CursosList /></PlanRoute></ProtectedRoute>} />

      <Route path="cursos/:id" element={<ProtectedRoute><PlanRoute><Curso /></PlanRoute></ProtectedRoute>} />

      <Route path="planos" element={<ProtectedRoute><Planos /></ProtectedRoute>} />

      <Route path="EsqueceuSenha" element={<EsqueceuSenha />} />
    </Route>
  )
);

export default router;
