import React, { useState } from 'react';
import { User, Settings, Bell, LogOut, X, ChevronRight, ChevronDown, Edit2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import Header from '../components/Header';
import Icon from '../assets/icon.png';

function Perfil() {
  const { signOut } = useAuth();
  const navigate = useNavigate();
  const [user, setUser] = useState({
    name: "Fulano de Tal",
    email: "fulano@exemplo.com",
    bio: "Sou Vendedor de carros",
    renda: "R$ 5.000,00",
    photoUrl: Icon
  });

  const [showNotificationMenu, setShowNotificationMenu] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert("Alterações salvas com sucesso!");
  };

  const handleLogout = async () => {
    const confirmLogout = window.confirm("Tem certeza que deseja sair da sua conta?");
    if (confirmLogout) {
      await signOut();
      navigate('/login');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 font-sans">
      <Header />

      <div className="flex items-center justify-center p-8">
        <div className="max-w-5xl w-full flex flex-col md:flex-row gap-6">

          {/* Coluna Esquerda - Menus */}
          <div className="w-full md:w-1/3 flex flex-col gap-6">

            {/* Menu Card */}
            <div className="bg-white rounded-2xl p-6 shadow-md">
              <div className="flex items-center gap-4 mb-6">
                <div className="w-12 h-12 rounded-full bg-gradient-to-br from-cyan-500 to-blue-600 flex items-center justify-center shadow-lg">
                  <User className="w-6 h-6 text-white" />
                </div>
                <div>
                  <h3 className="font-semibold text-gray-800">{user.name}</h3>
                  <p className="text-sm text-gray-500">{user.email}</p>
                </div>
              </div>

              <div className="border-t border-gray-100 my-4"></div>

              <nav className="flex flex-col gap-2">
                <button className="flex items-center justify-between w-full p-3 rounded-xl hover:bg-gray-50 transition-colors text-gray-700 bg-gray-50 font-medium">
                  <div className="flex items-center gap-3">
                    <User size={20} />
                    <span>Meu Perfil</span>
                  </div>
                  <ChevronRight size={18} className="text-gray-400" />
                </button>

                <button className="flex items-center justify-between w-full p-3 rounded-xl hover:bg-gray-50 transition-colors text-gray-700">
                  <div className="flex items-center gap-3">
                    <Settings size={20} />
                    <span>Configurações</span>
                  </div>
                  <ChevronRight size={18} className="text-gray-400" />
                </button>

                <div className="relative">
                  <button
                    className="flex items-center justify-between w-full p-3 rounded-xl hover:bg-gray-50 transition-colors text-gray-700"
                    onClick={() => setShowNotificationMenu(!showNotificationMenu)}
                  >
                    <div className="flex items-center gap-3">
                      <Bell size={20} />
                      <span>Notificações</span>
                    </div>
                    <span className="text-sm text-gray-500 mr-1">Ativo</span>
                  </button>
                  {/* Pseudo-dropdown para Notification */}
                  {showNotificationMenu && (
                    <div className="absolute right-0 top-full mt-1 bg-white shadow-xl rounded-xl border border-gray-100 p-2 z-10 w-32">
                      <button className="w-full text-left px-4 py-2 hover:bg-gray-50 rounded-lg text-sm text-gray-700 font-medium">Ativar</button>
                      <button className="w-full text-left px-4 py-2 hover:bg-gray-50 rounded-lg text-sm text-gray-700">Silenciar</button>
                    </div>
                  )}
                </div>

                <button 
                  onClick={handleLogout}
                  className="flex items-center justify-between w-full p-3 rounded-xl hover:bg-red-50 hover:text-red-600 transition-colors text-gray-700 mt-2"
                >
                  <div className="flex items-center gap-3">
                    <LogOut size={20} />
                    <span>Sair</span>
                  </div>
                </button>
              </nav>
            </div>



          </div>

          {/* Coluna Direita - Detalhes do Perfil */}
          <div className="w-full md:w-2/3 bg-white rounded-2xl p-8 shadow-md relative">
            <button className="absolute top-6 right-6 text-gray-400 hover:text-gray-600 transition-colors">
              <X size={20} />
            </button>

            <div className="flex items-center gap-6 mb-10">
              <div className="relative">
                <div className="w-20 h-20 rounded-full bg-gradient-to-br from-cyan-500 to-blue-600 flex items-center justify-center shadow-lg">
                  <User className="w-10 h-10 text-white" />
                </div>
                <button className="absolute bottom-0 right-0 bg-white p-1.5 rounded-full shadow-md border border-gray-100 hover:bg-blue-50 transition-colors">
                  <Edit2 size={14} className="text-blue-600" />
                </button>
              </div>
              <div>
                <h2 className="text-xl font-bold text-gray-800">{user.name}</h2>
                <p className="text-gray-500">{user.email}</p>
              </div>
            </div>

            <div className="border-t border-gray-100 my-6"></div>

            <form className="flex flex-col gap-6" onSubmit={handleSubmit}>

              <div className="flex flex-col md:flex-row md:items-center justify-between gap-2">
                <label className="text-gray-700 font-medium md:w-1/3">Nome</label>
                <input
                  type="text"
                  name="name"
                  className="md:w-2/3 bg-gray-50 border border-gray-200 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent text-gray-700 outline-none transition-shadow"
                  value={user.name}
                  onChange={handleChange}
                />
              </div>

              <div className="flex flex-col md:flex-row md:items-center justify-between gap-2">
                <label className="text-gray-700 font-medium md:w-1/3">E-mail</label>
                <input
                  type="email"
                  name="email"
                  className="md:w-2/3 bg-gray-50 border border-gray-200 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent text-gray-700 outline-none transition-shadow"
                  value={user.email}
                  onChange={handleChange}
                />
              </div>

              <div className="flex flex-col md:flex-row md:items-center justify-between gap-2">
                <label className="text-gray-700 font-medium md:w-1/3">Biografia</label>
                <input
                  type="text"
                  name="bio"
                  className="md:w-2/3 bg-gray-50 border border-gray-200 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent text-gray-700 outline-none transition-shadow"
                  value={user.bio}
                  onChange={handleChange}
                />
              </div>

              <div className="flex flex-col md:flex-row md:items-center justify-between gap-2">
                <label className="text-gray-700 font-medium md:w-1/3">Valor de Renda</label>
                <input
                  type="text"
                  name="renda"
                  className="md:w-2/3 bg-gray-50 border border-gray-200 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent text-gray-700 outline-none transition-shadow"
                  value={user.renda}
                  onChange={handleChange}
                />
              </div>

              <div className="border-t border-gray-50 mt-4"></div>

              <div className="mt-2 flex justify-end">
                <button
                  type="submit"
                  className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2.5 px-6 rounded-lg transition-colors shadow-sm"
                >
                  Salvar Alterações
                </button>
              </div>

            </form>

          </div>
        </div>
      </div>
    </div>
  );
}

export default Perfil;