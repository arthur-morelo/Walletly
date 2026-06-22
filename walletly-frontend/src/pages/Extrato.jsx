import React, { useState, useEffect, useRef } from "react";
import Header from "../components/Header";
import api from "../services/api";
import { UploadCloud, FileText, CheckCircle, XCircle, Clock, AlertCircle, Trash2 } from "lucide-react";

/**
 * @typedef {Object} ExtratoHistory
 * @property {number} id
 * @property {string} filename
 * @property {string} uploadDate
 * @property {'PENDENTE'|'PROCESSADO'|'ERRO'} status
 * @property {string} logMessage
 */

function ExtratosPage() {
  /** @type {[ExtratoHistory[], React.Dispatch<React.SetStateAction<ExtratoHistory[]>>]} */
  const [history, setHistory] = useState([]);
  const [isDragging, setIsDragging] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadError, setUploadError] = useState("");
  const fileInputRef = useRef(null);

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    try {
      const response = await api.get("/extratos/history");
      setHistory(response.data);
    } catch (error) {
      console.error("Erro ao carregar histórico", error);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Tem certeza que deseja deletar este extrato? As transações importadas também serão removidas do seu saldo.")) {
      return;
    }
    try {
      await api.delete(`/extratos/${id}`);
      loadHistory();
    } catch (error) {
      console.error("Erro ao deletar extrato", error);
      alert("Erro ao deletar extrato. Tente novamente.");
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = (e) => {
    e.preventDefault();
    setIsDragging(false);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragging(false);
    const files = e.dataTransfer.files;
    if (files && files.length > 0) {
      handleFileUpload(files[0]);
    }
  };

  const handleFileSelect = (e) => {
    const files = e.target.files;
    if (files && files.length > 0) {
      handleFileUpload(files[0]);
    }
  };

  /** @param {File} file */
  const handleFileUpload = async (file) => {
    setUploadError("");
    const validExtensions = [".ofx"];
    const isValidExtension = validExtensions.some(ext => file.name.toLowerCase().endsWith(ext));
    
    if (!isValidExtension && file.type !== "application/x-ofx" && file.type !== "text/sgml" && file.type !== "text/ofx") {
      setUploadError("Por favor, selecione apenas arquivos OFX.");
      return;
    }

    const token = localStorage.getItem('@Walletly:token');
    if (!token || token.trim() === '') {
      setUploadError("Token de autenticação não encontrado. Por favor, faça login novamente.");
      return;
    }

    setIsUploading(true);
    const formData = new FormData();
    formData.append("file", file);

    try {
      // Injetamos o token explicitamente no Axios e omitimos o Content-Type 
      // para que o navegador consiga calcular o boundary correto do form-data.
      await api.post("/extratos/upload", formData, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      loadHistory();
    } catch (error) {
      console.error("Erro no upload", error);
      const errorMsg = error.response?.data?.message || error.response?.data || error.message || "Ocorreu um erro ao enviar o arquivo.";
      setUploadError(typeof errorMsg === 'string' ? errorMsg : "Erro inesperado. Tente novamente.");
    } finally {
      setIsUploading(false);
      if (fileInputRef.current) fileInputRef.current.value = "";
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "PROCESSADO": return <CheckCircle className="text-green-500 w-5 h-5" />;
      case "ERRO": return <XCircle className="text-red-500 w-5 h-5" />;
      default: return <Clock className="text-yellow-500 w-5 h-5" />;
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "PROCESSADO": return <span className="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full font-medium">Processado</span>;
      case "ERRO": return <span className="bg-red-100 text-red-800 text-xs px-2 py-1 rounded-full font-medium">Erro</span>;
      default: return <span className="bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded-full font-medium">Pendente</span>;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="max-w-7xl mx-auto py-10 px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Extratos Bancários</h1>
          <p className="mt-2 text-gray-600">
            Faça upload dos seus extratos em OFX para alimentar automaticamente seu controle financeiro.
          </p>
        </div>

        {/* Upload Area */}
        <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-200 mb-10">
          <h2 className="text-xl font-semibold mb-6">Enviar Extrato</h2>
          
          <div
            className={`border-2 border-dashed rounded-xl p-10 flex flex-col items-center justify-center transition-colors cursor-pointer ${
              isDragging ? "border-blue-500 bg-blue-50" : "border-gray-300 hover:border-blue-400 hover:bg-gray-50"
            }`}
            onDragOver={handleDragOver}
            onDragLeave={handleDragLeave}
            onDrop={handleDrop}
            onClick={() => fileInputRef.current?.click()}
          >
            <input
              type="file"
              ref={fileInputRef}
              onChange={handleFileSelect}
              accept=".ofx,application/x-ofx,text/ofx"
              className="hidden"
            />
            <UploadCloud className={`w-12 h-12 mb-4 ${isDragging ? "text-blue-500" : "text-gray-400"}`} />
            <p className="text-lg font-medium text-gray-700">
              Arraste e solte seu extrato OFX aqui
            </p>
            <p className="text-sm text-gray-500 mt-1">
              ou clique para selecionar do seu computador
            </p>
            
            {isUploading && (
              <div className="mt-6 flex items-center text-blue-600">
                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-blue-600 mr-2"></div>
                Enviando arquivo...
              </div>
            )}
          </div>

          {uploadError && (
            <div className="mt-4 p-4 bg-red-50 border-l-4 border-red-500 flex items-start">
              <AlertCircle className="text-red-500 w-5 h-5 mr-2 mt-0.5" />
              <p className="text-red-700">{uploadError}</p>
            </div>
          )}
        </div>

        {/* History Table */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
          <div className="p-6 border-b border-gray-200">
            <h2 className="text-xl font-semibold">Histórico de Envios</h2>
          </div>
          
          {history.length > 0 ? (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Arquivo</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Data de Envio</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Mensagem</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ações</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {history.map((item) => (
                    <tr key={item.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <FileText className="text-gray-400 w-5 h-5 mr-3" />
                          <span className="text-sm font-medium text-gray-900">{item.filename}</span>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {new Date(item.uploadDate).toLocaleString("pt-BR")}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center gap-2">
                          {getStatusIcon(item.status)}
                          {getStatusBadge(item.status)}
                        </div>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500 max-w-2xl truncate" title={item.logMessage}>
                        {item.logMessage || "-"}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button
                          onClick={() => handleDelete(item.id)}
                          className="text-red-500 hover:text-red-700 transition-colors p-1 rounded-md hover:bg-red-50"
                          title="Deletar Arquivo e Transações"
                        >
                          <Trash2 className="w-5 h-5" />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="p-10 text-center flex flex-col items-center text-gray-500">
              <FileText className="w-12 h-12 text-gray-300 mb-3" />
              <p>Nenhum extrato enviado ainda.</p>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}

export default ExtratosPage;
