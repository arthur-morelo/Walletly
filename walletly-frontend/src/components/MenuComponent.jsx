import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { Menu, X, Target, BookOpen, PieChart, Receipt, GraduationCap } from "lucide-react";


function MenuComponent() {
  const [isOpen, setIsOpen] = useState(false);
  const location = useLocation();

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  const menuItems = [
    { to: "/Metas", icon: Target, label: "Metas" },
    { to: "/", icon: BookOpen, label: "Educação financeira" },
    { to: "/dashboard", icon: PieChart, label: "Controle de orçamento" },
    { to: "/extrato", icon: Receipt, label: "Extrato" },
    { to: "/cursos", icon: GraduationCap, label: "Cursos" }
  ];

  const isActive = (path) => location.pathname === path;

  return (
    <div>
      <button
        onClick={toggleMenu}
        className="focus:outline-none relative z-50 p-2 hover:bg-blue-900 rounded-lg transition-colors duration-200"
        aria-label="Toggle menu"
      >
        {isOpen ? (
          <X className="w-6 h-6 text-white" />
        ) : (
          <Menu className="w-6 h-6 text-white" />
        )}
      </button>

      {/* Overlay */}
      {isOpen && (
        <div 
          className="fixed inset-0 bg-black bg-opacity-50 z-30 transition-opacity duration-300"
          onClick={toggleMenu}
        />
      )}

      {/* Menu Sidebar */}
      <div 
        className={`fixed top-0 right-0 w-80 h-screen bg-gradient-to-b from-blue-950 via-blue-900 to-blue-950 shadow-2xl z-40 transform transition-transform duration-300 ease-in-out ${
          isOpen ? "translate-x-0" : "translate-x-full"
        }`}
      >
        <div className="flex flex-col h-full">
          {/* Header do Menu */}
          <div className="p-6 border-b border-blue-800">
            <h2 className="text-2xl font-bold text-white">Menu</h2>
            <p className="text-blue-300 text-sm mt-1">Navegue pelo Walletly</p>
          </div>

          {/* Items do Menu */}
          <nav className="flex-1 overflow-y-auto p-4">
            <ul className="flex flex-col gap-2">
              {menuItems.map((item) => {
                const Icon = item.icon;
                const active = isActive(item.to);
                
                return (
                  <li key={item.to}>
                    <Link
                      to={item.to}
                      onClick={toggleMenu}
                      className={`flex items-center gap-3 w-full p-4 rounded-xl text-white text-base font-medium transition-all duration-200 ${
                        active 
                          ? "bg-cyan-600 shadow-lg shadow-cyan-600/50 scale-105" 
                          : "bg-blue-800/50 hover:bg-blue-800 hover:scale-105 hover:shadow-lg"
                      }`}
                    >
                      <Icon className="w-5 h-5" />
                      <span>{item.label}</span>
                    </Link>
                  </li>
                );
              })}
            </ul>
          </nav>

          {/* Footer do Menu */}
          <div className="p-4 border-t border-blue-800">
            <div className="text-center text-blue-300 text-sm">
              <p>Walletly © 2024</p>
              <p className="text-xs mt-1">Seu assistente financeiro</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MenuComponent;