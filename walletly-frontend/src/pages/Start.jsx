import React from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  ArrowRight, 
  TrendingUp, 
  Target, 
  BookOpen, 
  CheckCircle2, 
  ShieldCheck 
} from 'lucide-react';
import Header from '../components/Header'; // Importando o componente Header existente
import logo from '../assets/logo.jpg';

function Start() {
    const navigate = useNavigate();

    return ( 
        <div className="flex flex-col min-h-screen bg-[#f8fafc]">
            {/* Header seguindo a estética do Header.jsx */}
            <Header /> 

            <main className="flex-grow">
                {/* Hero Section */}
                <section className="max-w-7xl mx-auto px-6 py-16 md:py-24 flex flex-col md:flex-row items-center gap-12">
                    <div className="md:w-1/2 space-y-8">
                        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-50 text-blue-600 text-sm font-medium border border-blue-100">
                            <ShieldCheck size={16} />
                            <span>Gestão Financeira Segura</span>
                        </div>
                        
                        <h1 className="text-5xl md:text-6xl font-bold text-gray-900 leading-[1.1]">
                            Tome o controle do seu <span className="text-blue-600">futuro financeiro.</span>
                        </h1>
                        
                        <p className="text-lg text-gray-600 leading-relaxed">
                            O Walletly ajuda você a monitorar gastos, definir metas de economia e aprender a investir melhor. Tudo em um só lugar.
                        </p>

                        <div className="flex flex-col sm:flex-row gap-4">
                            <button 
                                onClick={() => navigate('/cadastro')}
                                className="flex items-center justify-center gap-2 bg-blue-600 text-white px-8 py-4 rounded-xl font-bold hover:bg-blue-700 transition-all shadow-lg shadow-blue-200"
                            >
                                Comece já
                                <ArrowRight size={20} />
                            </button>
                            <button 
                                onClick={() => navigate('/')}
                                className="flex items-center justify-center px-8 py-4 rounded-xl font-bold border border-gray-200 text-gray-700 hover:bg-gray-50 transition-all"
                            >
                                Já tenho conta
                            </button>
                        </div>
                    </div>

                    {/* Espaço Ilustrativo (Mockup do App) */}
                    <div className="md:w-1/2 w-full relative">
                        <div className="bg-white rounded-3xl shadow-2xl border border-gray-100 p-4 overflow-hidden transform md:rotate-2 hover:rotate-0 transition-transform duration-500">
                            {/* Simulação de um mini-dashboard */}
                            <div className="flex gap-2 mb-4">
                                <div className="w-3 h-3 rounded-full bg-red-400"></div>
                                <div className="w-3 h-3 rounded-full bg-yellow-400"></div>
                                <div className="w-3 h-3 rounded-full bg-green-400"></div>
                            </div>
                            <div className="space-y-4">
                                <div className="h-8 w-1/3 bg-gray-100 rounded-lg"></div>
                                <div className="grid grid-cols-2 gap-4">
                                    <div className="h-32 bg-blue-50 rounded-2xl border border-blue-100 flex items-center justify-center">
                                        <TrendingUp className="text-blue-400" size={48} />
                                    </div>
                                    <div className="h-32 bg-green-50 rounded-2xl border border-green-100 flex items-center justify-center">
                                        <Target className="text-green-400" size={48} />
                                    </div>
                                </div>
                                <div className="h-40 bg-gray-50 rounded-2xl border border-gray-100"></div>
                            </div>
                        </div>
                        {/* Elemento flutuante de "Sucesso" */}
                        <div className="absolute -bottom-6 -left-6 bg-white p-4 rounded-2xl shadow-xl border border-gray-100 flex items-center gap-3 animate-bounce">
                            <div className="bg-green-500 p-2 rounded-full text-white">
                                <CheckCircle2 size={20} />
                            </div>
                            <div>
                                <p className="text-xs text-gray-500">Meta atingida!</p>
                                <p className="font-bold text-sm">Reserva de Emergência</p>
                            </div>
                        </div>
                    </div>
                </section>

                {/* Seção de Funcionalidades com Lucide */}
                <section className="bg-white py-20">
                    <div className="max-w-7xl mx-auto px-6">
                        <div className="text-center mb-16">
                            <h2 className="text-3xl font-bold text-gray-900">Por que escolher o Walletly?</h2>
                        </div>
                        
                        <div className="grid md:grid-cols-3 gap-12">
                            <FeatureCard 
                                icon={<TrendingUp className="text-blue-600" />}
                                title="Análise de Gastos"
                                description="Visualize para onde vai cada centavo com gráficos intuitivos e categorias personalizadas."
                            />
                            <FeatureCard 
                                icon={<Target className="text-green-600" />}
                                title="Metas de Economia"
                                description="Defina objetivos e acompanhe sua barra de progresso em tempo real."
                            />
                            <FeatureCard 
                                icon={<BookOpen className="text-purple-600" />}
                                title="Educação Financeira"
                                description="Acesse cursos exclusivos para dominar o mercado financeiro e investimentos."
                            />
                        </div>
                    </div>
                </section>
            </main>

            <footer className="bg-gray-50 border-t border-gray-100 py-12 text-center text-gray-500">
                <p>© 2024 Walletly. Desenvolvido para sua liberdade financeira.</p>
            </footer>
        </div>
     );
}

// Subcomponente para os cards de funcionalidade
function FeatureCard({ icon, title, description }) {
    return (
        <div className="group p-8 rounded-3xl hover:bg-blue-50 transition-all duration-300 border border-transparent hover:border-blue-100">
            <div className="w-14 h-14 bg-gray-50 rounded-2xl flex items-center justify-center mb-6 group-hover:bg-white group-hover:shadow-md transition-all">
                {icon}
            </div>
            <h3 className="text-xl font-bold mb-3 text-gray-900">{title}</h3>
            <p className="text-gray-600 leading-relaxed">{description}</p>
        </div>
    );
}

export default Start;