// Header.jsx
import React from "react";
import Menu from "./MenuComponent";
import Icon from "../assets/icon.png";
import { User } from "lucide-react";
import { Link, useLocation } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

const Header = () => {
  const { user } = useAuth();
  const userName = user?.name || user?.nome || "Usuário";

  return (
    <header className="bg-gradient-to-r from-blue-950 via-blue-900 to-blue-950 text-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 py-3">
        <div className="flex justify-between items-center">
          {/* User Info */}
          <Link to="/perfil">
            <div className="flex items-center space-x-3 group cursor-pointer">
              <div className="relative">
                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-cyan-500 to-blue-600 flex items-center justify-center shadow-lg group-hover:shadow-cyan-500/50 transition-shadow duration-300">
                  <User className="w-5 h-5 text-white" />
                </div>
                <div className="absolute -bottom-1 -right-1 w-3 h-3 bg-green-400 rounded-full border-2 border-blue-950"></div>
              </div>
              <div className="hidden sm:block">
                <p className="text-xs text-blue-300">Olá,</p>
                <p className="text-sm font-semibold">{userName}</p>
              </div>
            </div>
          </Link>

          {/* Logo Central */}
          <div className="hidden md:flex flex-row items-center space-x-3 absolute left-1/2 transform -translate-x-1/2">
            <img 
              src={Icon} 
              alt="Logo Walletly" 
              className="w-12 h-12 rounded-xl shadow-lg object-cover" 
            />
            <div>
              <span className="text-2xl font-bold bg-gradient-to-r from-cyan-400 to-blue-300 bg-clip-text text-transparent">
                Walletly
              </span>
              <p className="text-xs text-blue-300 -mt-1">Finance App</p>
            </div>
          </div>

          {/* Menu Mobile Logo */}
          <div className="md:hidden flex items-center space-x-2">
            <img 
              src={Icon} 
              alt="Logo" 
              className="w-8 h-8 rounded-lg object-cover" 
            />
            <span className="text-lg font-bold">Walletly</span>
          </div>

          {/* Menu Button */}
          <div className="order-1 md:order-3">
            <Menu />
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;