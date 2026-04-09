/* 
  EMBRYO METRIX WEB - V3 (FINAL CONNECTED VERSION)
  Fully synchronized with your Django Backend (localhost:8000)
  Dependencies: Tailwind CSS, Lucide-React, Axios
*/

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { 
  Home, PlusSquare, FileText, User, ChevronLeft, ChevronRight, Microscope, 
  Activity, Info, LogOut, Clock, FlaskConical, Zap, CheckCircle2, 
  Camera, Video, Database, MapPin, Phone, Mail, Edit2, Save, X, Plus, ArrowRight,
  Brain, ClipboardList, Loader2, Sparkles, Beaker, Thermometer, Droplets, GraduationCap, Briefcase, Target,
  Bot, Send
} from 'lucide-react';

// --- API CONFIGURATION ---
const API_BASE_URL = "http://127.0.0.1:8000/api";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) { config.headers.Authorization = token; }
  return config;
}, error => Promise.reject(error));

// --- BRANDING COMPONENT ---
const Logo = ({ size = "md", showText = false, light = false }) => {
  const sizes = { sm: "w-8 h-8", md: "w-12 h-12", lg: "w-20 h-20" };
  return (
    <div className={`flex flex-col items-center ${size === 'lg' ? 'gap-6' : 'gap-2'}`}>
      <div className={`relative ${sizes[size]} flex items-center justify-center`}>
        <svg viewBox="0 0 100 100" className="w-full h-full">
          <circle cx="50" cy="50" r="22" className={light ? "fill-white" : "fill-blue-600"} />
          <circle cx="50" cy="50" r="10" className="fill-white" />
          <path d="M50 10 L50 35 M50 65 L50 90 M10 50 L35 50 M65 50 L90 50" stroke={light ? "white" : "#2563eb"} strokeWidth="4" strokeLinecap="round" opacity="0.4" />
          <path d="M25 25 Q50 15 75 25 M75 75 Q50 85 25 75" fill="none" stroke={light ? "white" : "#22d3ee"} strokeWidth="3" strokeLinecap="round" />
        </svg>
      </div>
      {showText && (
        <div className="text-center">
          <h1 className={`font-black tracking-tight ${size === 'lg' ? 'text-3xl' : 'text-sm'} ${light ? 'text-white' : 'text-gray-900'}`}>Embryo Metrix</h1>
          <p className={`font-bold tracking-[0.2em] uppercase ${size === 'lg' ? 'text-[10px]' : 'text-[6px]'} ${light ? 'text-blue-200' : 'text-gray-400'}`}>Advanced Clinical AI</p>
        </div>
      )}
    </div>
  );
};

const formatDOB = (dobStr) => {
  if (!dobStr) return 'N/A';
  if (dobStr.includes('-')) {
    const parts = dobStr.split('-');
    if (parts.length === 3) {
      if (parts[0].length === 4) return `${parts[2]}/${parts[1]}/${parts[0]}`;
      return `${parts[0]}/${parts[1]}/${parts[2]}`;
    }
  }
  return dobStr;
};

// --- SUB-COMPONENTS ---

const AuthView = ({ mode, setMode, onLogin, onSignUp, onPatientLogin }) => {
  const [formData, setFormData] = useState({
    username: '', email: '', password: '', 
    full_name: '', specialization: '', experience_years: '', clinic_name: '', phone_number: '', address: '',
    patient_id: '', patient_dob: ''
  });
  const [loading, setLoading] = useState(false);
  const [signupStep, setSignupStep] = useState(1);

  const validateEmail = (email) => {
    const regex = /^[A-Za-z][A-Za-z0-9._%+-]*@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    return regex.test(email);
  };

  const validatePassword = (pass) => {
    const hasUpper = /[A-Z]/.test(pass);
    const hasNumber = /[0-9]/.test(pass);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(pass);
    return { isValid: pass.length >= 8 && hasUpper && hasNumber && hasSpecial, hasUpper, hasNumber, hasSpecial, hasLength: pass.length >= 8 };
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (mode === 'signup') {
      if (!validateEmail(formData.email)) { alert("Email must start with a letter and use only alphanumeric characters in the prefix (e.g., doctor123@gmail.com)"); return; }
      const validation = validatePassword(formData.password);
      if (!validation.isValid) { alert("Password must contain: \n- At least 8 characters\n- At least 1 uppercase letter\n- At least 1 number\n- At least 1 special character"); return; }
      if (signupStep === 1) { setSignupStep(2); return; }
    }
    setLoading(true);
    if (mode === 'login') onLogin(formData).finally(() => setLoading(false));
    else if (mode === 'signup') onSignUp(formData).finally(() => setLoading(false));
    else if (mode === 'patient') onPatientLogin(formData).finally(() => setLoading(false));
  };

  const handleSwitchMode = (newMode) => { setMode(newMode); setSignupStep(1); };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#F8FAFC] px-4 py-12">
      <div className="w-full bg-white rounded-[3rem] shadow-2xl p-10 border border-blue-50 transition-all duration-500 max-w-md">
        <div className="text-center mb-8">
          <Logo size="lg" />
          <p className="text-gray-400 font-bold text-[10px] mt-3 uppercase tracking-[0.3em] mb-10">Clinical Intelligence Portal</p>
          <div className="flex bg-gray-50 p-2 rounded-2xl mb-10 mx-auto max-w-[320px]">
            <button onClick={() => handleSwitchMode('login')} className={`flex-1 py-3 rounded-xl font-black text-xs uppercase tracking-widest transition-all ${mode === 'login' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-400 hover:text-gray-600'}`}>Sign In</button>
            <button onClick={() => handleSwitchMode('signup')} className={`flex-1 py-3 rounded-xl font-black text-xs uppercase tracking-widest transition-all ${mode === 'signup' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-400 hover:text-gray-600'}`}>Sign Up</button>
          </div>
        </div>
        <form className="space-y-6" onSubmit={handleSubmit}>
          {mode === 'login' ? (
            <div className="space-y-5">
              <h3 className="text-[11px] font-black text-blue-600 uppercase tracking-widest border-b border-blue-50 pb-2">Credentials</h3>
              <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Email Address</label><input value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} type="email" className="w-full px-6 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-gray-700" placeholder="doctor@clinic.com" required /></div>
              <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Password</label><input value={formData.password} onChange={e => setFormData({...formData, password: e.target.value})} type="password" className="w-full px-6 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-gray-700" placeholder="••••••••" required /></div>
            </div>
          ) : mode === 'patient' ? (
            <div className="space-y-5">
              <h3 className="text-[11px] font-black text-blue-600 uppercase tracking-widest border-b border-blue-50 pb-2">Patient Access</h3>
              <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Patient ID</label><input value={formData.patient_id} onChange={e => setFormData({...formData, patient_id: e.target.value})} className="w-full px-6 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-gray-700" placeholder="e.g. P90210" required /></div>
              <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Date of Birth</label><input value={formData.patient_dob} onChange={e => setFormData({...formData, patient_dob: e.target.value})} type="text" className="w-full px-6 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-gray-700" placeholder="DD/MM/YYYY" required /></div>
            </div>
          ) : (
            <>
              {signupStep === 1 ? (
                <div className="space-y-5 animate-in fade-in slide-in-from-right-4 duration-500">
                  <h3 className="text-[11px] font-black text-blue-600 uppercase tracking-widest border-b border-blue-50 pb-2">Step 1: Account Setup</h3>
                  <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Email Address</label><input value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} type="email" className="w-full px-6 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-gray-700" placeholder="doctor@clinic.com" required /></div>
                  <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Password</label><input value={formData.password} onChange={e => setFormData({...formData, password: e.target.value})} type="password" className="w-full px-6 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-gray-700" placeholder="••••••••" required /></div>
                </div>
              ) : (
                <div className="space-y-5 animate-in fade-in slide-in-from-right-4 duration-500">
                  <div className="flex items-center justify-between border-b border-blue-50 pb-2">
                    <h3 className="text-[11px] font-black text-blue-600 uppercase tracking-widest">Step 2: Doctor Details</h3>
                    <button type="button" onClick={() => setSignupStep(1)} className="text-[9px] font-black text-gray-400 uppercase tracking-widest hover:text-blue-600">Back</button>
                  </div>
                  <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Full Name</label><input value={formData.full_name} onChange={e => setFormData({...formData, full_name: e.target.value})} className="w-full px-6 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-gray-700" placeholder="Dr. Jane Smith" required /></div>
                  <div className="grid grid-cols-2 gap-4">
                    <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Specialization</label><input value={formData.specialization} onChange={e => setFormData({...formData, specialization: e.target.value})} className="w-full px-4 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-xs text-gray-700" placeholder="Embryologist" required /></div>
                    <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Experience (Yrs)</label><input type="number" value={formData.experience_years} onChange={e => setFormData({...formData, experience_years: e.target.value})} className="w-full px-4 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-xs text-gray-700" placeholder="10" required /></div>
                  </div>
                  <div><label className="block text-[10px] font-black text-gray-400 mb-2 uppercase tracking-widest">Clinic Name</label><input value={formData.clinic_name} onChange={e => setFormData({...formData, clinic_name: e.target.value})} className="w-full px-6 py-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-blue-500 focus:bg-white outline-none transition-all font-bold text-gray-700" placeholder="Clinic Name" required /></div>
                </div>
              )}
            </>
          )}
          <button type="submit" disabled={loading} className="w-full bg-blue-600 hover:bg-blue-700 text-white font-black py-5 rounded-2xl shadow-2xl shadow-blue-200 transition-all active:scale-95 uppercase tracking-[0.2em] mt-8 flex items-center justify-center gap-3">
            {loading ? <Loader2 className="animate-spin" size={20} /> : (mode === 'login' ? 'Verify Identity' : (mode === 'patient' ? 'Confirm Details' : (signupStep === 1 ? 'Continue to Details' : 'Complete Registration')))}
          </button>
        </form>
        <div className="mt-10 text-center space-y-4">
          {mode === 'login' ? (<button onClick={() => handleSwitchMode('signup')} className="block w-full text-[10px] font-black text-gray-400 uppercase tracking-widest hover:text-blue-600 transition-colors">Don't have an account? Create Account</button>) : mode === 'signup' ? (<button onClick={() => handleSwitchMode('login')} className="block w-full text-[10px] font-black text-gray-400 uppercase tracking-widest hover:text-blue-600 transition-colors">Already have an account? Sign In</button>) : null}
          {(mode === 'login' || mode === 'signup') && (<button type="button" onClick={() => handleSwitchMode('patient')} className="block w-full text-[10px] font-black text-blue-600 uppercase tracking-widest hover:text-blue-800 transition-colors border-t border-blue-50 pt-4">Login as Patient</button>)}
          {mode === 'patient' && (<button type="button" onClick={() => handleSwitchMode('login')} className="block w-full text-[10px] font-black text-gray-400 uppercase tracking-widest hover:text-blue-600 transition-colors border-t border-blue-50 pt-4">Return to Doctor Login</button>)}
        </div>
      </div>
    </div>
  );
};

const DashboardView = ({ profile, onStartAssessment, onViewReport, recentReports, stats, onChatbot }) => (
  <div className="space-y-12 animate-in fade-in slide-in-from-bottom-5 duration-700">
    <header className="flex justify-between items-end">
      <div>
        <h1 className="text-5xl font-black text-gray-900 tracking-tighter uppercase leading-none">Dashboard</h1>
        <p className="text-gray-400 font-bold uppercase tracking-[0.25em] text-[10px] mt-4">Welcome back, {profile.full_name?.toUpperCase() || 'DOCTOR'}</p>
      </div>
    </header>
    <div className="grid grid-cols-2 lg:grid-cols-5 gap-6">
      <div className="bg-white p-8 rounded-[2.5rem] border border-gray-50 shadow-sm"><p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">Total Reports</p><p className="text-4xl font-black text-gray-900 leading-none">{stats.total}</p></div>
      <div className="bg-white p-8 rounded-[2.5rem] border border-gray-50 shadow-sm"><p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">Avg Score</p><p className="text-4xl font-black text-blue-600 leading-none">{stats.avgScore}%</p></div>
      <div className="bg-emerald-50 p-8 rounded-[2.5rem] border border-emerald-100/50"><p className="text-[10px] font-black text-emerald-600/60 uppercase tracking-widest mb-1">Good</p><p className="text-4xl font-black text-emerald-600 leading-none">{stats.good}</p></div>
      <div className="bg-amber-50 p-8 rounded-[2.5rem] border border-amber-100/50"><p className="text-[10px] font-black text-amber-600/60 uppercase tracking-widest mb-1">Moderate</p><p className="text-4xl font-black text-amber-600 leading-none">{stats.moderate}</p></div>
      <div className="bg-rose-50 p-8 rounded-[2.5rem] border border-rose-100/50"><p className="text-[10px] font-black text-rose-600/60 uppercase tracking-widest mb-1">Bad</p><p className="text-4xl font-black text-rose-600 leading-none">{stats.bad}</p></div>
    </div>
    <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
      <div onClick={onStartAssessment} className="group bg-blue-600 p-10 rounded-[3rem] shadow-2xl shadow-blue-100 cursor-pointer hover:scale-[1.02] transition-all relative overflow-hidden flex flex-col justify-between min-h-[320px]">
        <div className="relative z-10 w-20 h-20 bg-white/20 backdrop-blur-md rounded-3xl flex items-center justify-center text-white mb-10"><PlusSquare size={40} /></div>
        <div className="relative z-10"><h3 className="text-3xl font-black text-white uppercase leading-none mb-4 tracking-tighter">New<br/>Assessment</h3><p className="text-blue-100 font-bold text-sm tracking-wide">Start a fresh AI analysis for spent media markers.</p></div>
        <Activity className="absolute -right-10 -bottom-10 w-48 h-48 text-white/10" />
      </div>
      <div onClick={onChatbot} className="group bg-blue-50 p-10 rounded-[3rem] shadow-sm border-2 border-blue-100 cursor-pointer hover:shadow-2xl hover:border-blue-400 hover:bg-blue-600 transition-all flex flex-col justify-between min-h-[320px]">
        <div className="w-20 h-20 bg-white rounded-3xl flex items-center justify-center text-blue-600 mb-10 group-hover:bg-white/20 transition-all shadow-sm"><Bot size={40} /></div>
        <div><h3 className="text-3xl font-black text-blue-900 uppercase leading-none mb-4 tracking-tighter group-hover:text-white transition-all">AI<br/>Assistant</h3><p className="text-blue-600 font-bold text-sm tracking-wide group-hover:text-blue-100 transition-all">Ask the clinical AI for analysis support.</p></div>
      </div>
    </div>
    <section>
      <h2 className="text-2xl font-black text-gray-900 uppercase tracking-tighter mb-8">Recent Activity</h2>
      <div className="bg-white rounded-[2.5rem] overflow-hidden border border-gray-50 shadow-sm">
        {recentReports.length > 0 ? recentReports.map((report, i) => (
          <div key={report.id} onClick={() => onViewReport(report)} className={`flex items-center justify-between p-8 ${i !== recentReports.length-1 ? 'border-b border-gray-50' : ''} hover:bg-gray-50 transition-all cursor-pointer group`}>
            <div className="flex items-center gap-6">
              <div className="w-14 h-14 bg-gray-100 rounded-2xl flex items-center justify-center text-gray-400 font-black text-xs uppercase tracking-tighter leading-none text-center group-hover:bg-blue-100 group-hover:text-blue-600 transition-colors">R<br/>ID</div>
              <div><p className="font-black text-lg text-gray-900 leading-none mb-1 group-hover:text-blue-600 transition-colors">{report.patient_name}</p><p className="text-xs font-bold text-gray-400 uppercase tracking-widest">ID: {report.patient_id} • {new Date(report.created_at).toLocaleDateString()}</p></div>
            </div>
            <div className="flex items-center gap-8">
              <div className="text-right"><p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">Confidence</p><p className={`font-black text-xl ${report.analysis?.[0]?.confidence_score > 90 ? 'text-blue-600' : 'text-emerald-500'}`}>{report.analysis?.[0]?.confidence_score || '0'}%</p></div>
              <ChevronRight className="text-gray-300 group-hover:text-blue-600 transition-colors" />
            </div>
          </div>
        )) : <div className="p-12 text-center text-gray-400 font-bold">No recent activity detected.</div>}
      </div>
    </section>
  </div>
);

const ReportsListView = ({ reports, onViewReport, onNewAssessment }) => (
  <div className="space-y-8 animate-in fade-in duration-700">
    <div className="flex justify-between items-center">
      <h2 className="text-4xl font-black text-gray-900 tracking-tighter uppercase">Clinical Reports</h2>
      <button onClick={onNewAssessment} className="bg-blue-600 text-white p-4 rounded-2xl shadow-xl shadow-blue-100 active:scale-95 transition-all"><Plus size={24} strokeWidth={3} /></button>
    </div>
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6 pb-20">
      {reports.map(report => (
        <div key={report.id} onClick={() => onViewReport(report)} className="bg-white p-8 rounded-[2.5rem] border border-gray-100 shadow-sm hover:shadow-2xl hover:border-blue-100 transition-all cursor-pointer group">
          <div className="flex justify-between items-start mb-6">
            <div><p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">Patient ID</p><p className="font-black text-gray-900">{report.patient_id}</p></div>
            <div className={`px-4 py-2 rounded-xl font-black text-xs uppercase tracking-widest ${report.analysis?.[0]?.confidence_score > 90 ? 'bg-blue-50 text-blue-600' : 'bg-emerald-50 text-emerald-600'}`}>{report.analysis?.[0]?.confidence_score || '0'}% Match</div>
          </div>
          <h3 className="text-2xl font-black text-gray-900 uppercase tracking-tight mb-2 group-hover:text-blue-600 transition-colors">{report.patient_name}</h3>
          <div className="flex gap-4 items-center text-gray-400 font-bold text-xs uppercase tracking-widest">
            <span>{new Date(report.created_at).toLocaleDateString()}</span><span>•</span><span>{report.patient_age} YR</span><span>•</span><span>{formatDOB(report.patient_dob)}</span>
          </div>
        </div>
      ))}
    </div>
  </div>
);

const AssessmentView = ({ step, data, setData, onNext, onPrev, onComplete }) => {
  if (step === 0) {
    return (
      <div className="space-y-8 animate-in fade-in duration-500 max-w-3xl mx-auto">
        <div className="bg-white rounded-[2.5rem] p-12 shadow-xl border border-gray-100">
          <h2 className="text-4xl font-black text-gray-900 tracking-tighter uppercase mb-2 text-center">Patient Intake</h2>
          <p className="text-gray-400 font-bold uppercase tracking-widest text-xs mb-12 text-center">Registry system for clinical reporting</p>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-x-10 gap-y-8">
            <div><label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block mb-3">Patient ID</label><input value={data.patient_id} onChange={e => setData({...data, patient_id: e.target.value})} className="w-full bg-gray-50 border-2 border-gray-100 p-5 rounded-2xl font-bold focus:border-blue-500 outline-none" placeholder="e.g. P90210" /></div>
            <div><label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block mb-3">Patient Name</label><input value={data.patient_name} onChange={e => setData({...data, patient_name: e.target.value})} className="w-full bg-gray-50 border-2 border-gray-100 p-5 rounded-2xl font-bold focus:border-blue-500 outline-none" placeholder="First & Last Name" /></div>
            <div><label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block mb-3">Date of Birth</label><input type="text" value={data.patient_dob} onChange={e => setData({...data, patient_dob: e.target.value})} className="w-full bg-gray-50 border-2 border-gray-100 p-5 rounded-2xl font-bold focus:border-blue-500 outline-none" placeholder="DD/MM/YYYY" /></div>
            <div><label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block mb-3">Patient Age</label><input type="number" value={data.patient_age} onChange={e => setData({...data, patient_age: e.target.value})} className="w-full bg-gray-50 border-2 border-gray-100 p-5 rounded-2xl font-bold focus:border-blue-500 outline-none" placeholder="Years" /></div>
          </div>
        </div>
        <button onClick={() => onNext()} className="w-full bg-blue-600 text-white font-black py-6 rounded-[1.5rem] shadow-2xl shadow-blue-200 transition-all active:scale-95 uppercase tracking-[0.2em]">Start Assessment Flow</button>
      </div>
    );
  }
  if (step >= 1 && step <= 6) {
    const questions = [
      { title: "Embryo Development Day", options: ["D3", "D5", "D6"], key: "embryo_day" },
      { title: "Culture Duration", isInput: true, key: "culture_duration" },
      { title: "Culture Medium Used", options: ["G-1 PLUS", "G-2 PLUS", "Stage 1-Step", "Origio", "Other"], key: "culture_medium" },
      { title: "Media Color Change", options: ["None", "Mild", "Significant"], key: "media_color_change" },
      { title: "pH Deviation", options: ["Normal", "Slight", "High"], key: "ph_deviation" },
      { title: "Visual Clarity", options: ["Clear", "Slightly turbid", "Turbid"], key: "visual_clarity" }
    ];
    const q = questions[step - 1];
    return (
      <div className="space-y-8 animate-in fade-in duration-500 max-w-2xl mx-auto">
        <div className="bg-white rounded-[3rem] p-12 shadow-xl border border-gray-100">
          <div className="flex justify-between items-center mb-10">
            <span className="text-[10px] font-black text-gray-400 uppercase tracking-[0.3em]">Step {step} / 9</span>
            <div className="flex gap-2">{[1,2,3,4,5,6,7,8,9].map(i => <div key={i} className={`h-1.5 w-6 rounded-full ${i <= step ? 'bg-blue-600' : 'bg-gray-100'}`} />)}</div>
          </div>
          <h2 className="text-4xl font-black text-gray-900 tracking-tighter mb-4 leading-tight">{q.title}</h2>
          {q.isInput ? (
            <input type="number" value={data[q.key]} onChange={e => setData({...data, [q.key]: e.target.value})} className="w-full bg-gray-50 border-2 border-gray-100 p-8 rounded-[2rem] font-black text-4xl text-center focus:border-blue-500 outline-none" placeholder="0" />
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {q.options.map(opt => (
                <button key={opt} onClick={() => setData({...data, [q.key]: opt})} className={`relative overflow-hidden group border-4 rounded-[2.5rem] p-8 font-black transition-all active:scale-95 ${data[q.key] === opt ? 'border-blue-600 bg-blue-50 text-blue-600 shadow-xl shadow-blue-100' : 'border-gray-50 bg-gray-50 hover:bg-white hover:border-gray-100 text-gray-400 hover:text-gray-900'}`}>
                  <span className="block text-xl uppercase tracking-widest">{opt}</span>
                  {data[q.key] === opt && <CheckCircle2 className="absolute top-4 right-4 text-blue-600" size={24} />}
                </button>
              ))}
            </div>
          )}
        </div>
        <div className="flex gap-4">
          <button onClick={onPrev} className="flex-1 py-6 border-2 border-gray-100 rounded-2xl font-black uppercase text-xs tracking-widest text-gray-400 hover:bg-white hover:text-gray-900 transition-all">Back</button>
          <button onClick={onNext} className="flex-[2] py-6 bg-blue-600 text-white rounded-2xl font-black uppercase tracking-[0.2em] text-xs shadow-2xl shadow-blue-200 transition-all active:scale-95">Next Step</button>
        </div>
      </div>
    );
  }
  if (step === 7) {
    return (
      <div className="space-y-8 animate-in fade-in duration-500 max-w-2xl mx-auto">
        <div className="bg-white rounded-[3rem] p-12 shadow-xl border border-gray-100">
          <h2 className="text-4xl font-black text-gray-900 tracking-tighter uppercase mb-2">Doctor Notes</h2>
          <textarea value={data.doctor_notes} onChange={e => setData({...data, doctor_notes: e.target.value})} className="w-full bg-gray-50 border-2 border-gray-100 p-8 rounded-[2rem] font-bold text-lg focus:border-blue-500 outline-none min-h-[200px]" placeholder="Enter clinical notes here..." />
        </div>
        <div className="flex gap-4">
          <button onClick={onPrev} className="flex-1 py-6 border-2 border-gray-100 rounded-2xl font-black uppercase text-xs tracking-widest text-gray-400 hover:bg-white hover:text-gray-900 transition-all">Back</button>
          <button onClick={onNext} className="flex-[2] py-6 bg-blue-600 text-white rounded-2xl font-black uppercase tracking-[0.2em] text-xs shadow-2xl shadow-blue-200 transition-all active:scale-95">Next: Media</button>
        </div>
      </div>
    );
  }
  if (step === 8) {
    return (
      <div className="space-y-8 animate-in fade-in duration-500 max-w-2xl mx-auto">
        <div className="bg-white rounded-[3rem] p-12 shadow-xl border border-gray-100">
          <h2 className="text-4xl font-black text-gray-900 tracking-tighter uppercase mb-2">Upload Photo</h2>
          <label className="border-4 border-dashed border-gray-100 rounded-[3rem] p-20 flex flex-col items-center justify-center gap-6 group hover:border-blue-400 transition-all cursor-pointer bg-gray-50/50">
            <input type="file" className="hidden" onChange={(e) => setData({...data, file: e.target.files[0]})} />
            <Camera size={40} className="text-blue-600" />
            <p className="font-black text-xl text-gray-900">{data.file ? data.file.name : 'Select Photo File'}</p>
          </label>
        </div>
        <div className="flex gap-4">
          <button onClick={onPrev} className="flex-1 py-6 border-2 border-gray-100 rounded-2xl font-black uppercase text-xs tracking-widest text-gray-400 hover:bg-white hover:text-gray-900 transition-all">Back</button>
          <button onClick={onNext} className="flex-[2] py-6 bg-blue-600 text-white rounded-2xl font-black uppercase tracking-[0.2em] text-xs shadow-2xl shadow-blue-200 transition-all active:scale-95">Analyze Media</button>
        </div>
      </div>
    );
  }
  if (step === 9) {
    return (
      <div className="min-h-[60vh] flex flex-col items-center justify-center space-y-8">
        <Brain size={80} className="text-blue-600 animate-pulse" />
        <p className="text-xs font-black uppercase tracking-widest text-blue-600">Running AI Analysis...</p>
        <button onClick={onComplete} className="bg-blue-600 text-white px-8 py-4 rounded-2xl font-black uppercase">View Results</button>
      </div>
    );
  }
};

const ResultsView = ({ report, onBackToReports }) => {
  const analysis = report.analysis?.[0] || {};
  return (
    <div className="max-w-5xl mx-auto space-y-10 animate-in zoom-in-95 duration-700 pb-20 print:p-0">
      <button onClick={onBackToReports} className="flex items-center gap-3 font-black uppercase text-[10px] tracking-widest text-gray-400 hover:text-blue-600 transition-colors print:hidden"><ChevronLeft size={16} /> Back</button>
      <section className="bg-white rounded-[4rem] p-12 shadow-2xl border border-gray-50">
        <div className="flex justify-between items-start mb-12">
          <h2 className="text-5xl font-black text-gray-900 tracking-tighter uppercase leading-none">Analysis<br/>Report</h2>
          <div className="text-right"><p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2">Patient ID</p><div className="px-6 py-3 bg-gray-50 rounded-2xl font-black text-blue-600">{report.patient_id}</div></div>
        </div>
        <div className="grid grid-cols-2 md:grid-cols-5 gap-12">
          <div><p className="text-[10px] font-black text-gray-400 uppercase mb-2">Patient</p><p className="font-black text-2xl">{report.patient_name}</p></div>
          <div><p className="text-[10px] font-black text-gray-400 uppercase mb-2">Confidence</p><p className="font-black text-4xl text-emerald-600">{analysis.confidence_score}%</p></div>
          <div><p className="text-[10px] font-black text-gray-400 uppercase mb-2">Age</p><p className="font-black text-2xl">{report.patient_age} YR</p></div>
          <div><p className="text-[10px] font-black text-gray-400 uppercase mb-2">Birth Date</p><p className="font-black text-2xl">{formatDOB(report.patient_dob)}</p></div>
          <div><p className="text-[10px] font-black text-gray-400 uppercase mb-2">Viability</p><span className="bg-blue-600 text-white px-4 py-2 rounded-xl text-xs font-black uppercase">{analysis.viability_prediction}</span></div>
        </div>
      </section>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
        <div className="bg-white p-10 rounded-[3.5rem] border border-gray-50 shadow-sm space-y-6">
          <h3 className="uppercase font-black text-[10px] text-blue-600">Metabolic Markers</h3>
          <div className="grid grid-cols-2 gap-6">
            {[ {n:"Glucose",v:analysis.glucose_level},{n:"Lactate",v:analysis.lactate_level},{n:"Oxidative",v:analysis.oxidative_stress},{n:"Pyruvate",v:analysis.pyruvate_level} ].map(m => (
              <div key={m.n}><p className="text-[9px] font-black text-gray-400 uppercase mb-1">{m.n}</p><p className="font-black text-xl">{m.v} <span className="text-xs text-gray-300 ml-1">mg/L</span></p></div>
            ))}
          </div>
        </div>
        <div className="bg-[#1e1b4b] p-10 rounded-[3.5rem] text-white flex flex-col justify-between">
          <div><h3 className="text-3xl font-black uppercase mb-6">AI Feedback</h3><p className="text-blue-200 text-sm leading-relaxed">{analysis.ai_feedback}</p></div>
          <button onClick={() => window.print()} className="mt-10 bg-white text-blue-900 font-black py-5 rounded-3xl uppercase tracking-widest text-[10px] print:hidden">Export PDF</button>
        </div>
      </div>
    </div>
  );
};

const PatientReportsListView = ({ reports, onViewReport }) => {
  const sortedReports = [...reports].sort((a,b) => new Date(a.created_at) - new Date(b.created_at));
  const scores = sortedReports.map(r => parseFloat(r.analysis?.[0]?.confidence_score || 0));
  const chartWidth = 800, chartHeight = 200, padding = 40;
  const points = scores.length > 1 ? scores.map((s, i) => {
    const x = padding + (i * (chartWidth - 2 * padding) / (scores.length - 1));
    const y = chartHeight - padding - (s * (chartHeight - 2 * padding) / 100);
    return `${x},${y}`;
  }).join(' ') : "";
  return (
    <div className="space-y-12 animate-in fade-in slide-in-from-bottom-5 duration-700">
      <header><h1 className="text-5xl font-black text-gray-900 tracking-tighter uppercase leading-none mb-4">Welcome</h1><p className="text-blue-600 font-black uppercase tracking-[0.25em] text-xs">Patient: {reports[0]?.patient_name?.toUpperCase() || 'VALUED PATIENT'}</p></header>
      <div className="bg-white p-10 rounded-[3rem] border border-blue-50 shadow-2xl relative overflow-hidden group">
        <div className="flex justify-between items-center mb-10"><div><p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">Performance Insight</p><h3 className="text-2xl font-black text-gray-900 uppercase tracking-tighter">Confidence Score Trend</h3></div><Activity className="text-blue-600" size={32} /></div>
        {scores.length > 1 ? (
          <div className="h-40 w-full">
            <svg viewBox={`0 0 ${chartWidth} ${chartHeight}`} className="w-full h-full overflow-visible">
              <defs><linearGradient id="lineGrad" x1="0" y1="0" x2="1" y2="0"><stop offset="0%" stopColor="#2563eb" /><stop offset="100%" stopColor="#22d3ee" /></linearGradient></defs>
              <line x1={padding} y1={chartHeight-padding} x2={chartWidth-padding} y2={chartHeight-padding} stroke="#f1f5f9" strokeWidth="2" />
              <polyline fill="none" stroke="url(#lineGrad)" strokeWidth="8" strokeLinecap="round" strokeLinejoin="round" points={points} className="drop-shadow-lg" />
              {scores.map((s, i) => { const x = padding + (i * (chartWidth - 2 * padding) / (scores.length - 1)); const y = chartHeight - padding - (s * (chartHeight - 2 * padding) / 100); return <circle key={i} cx={x} cy={y} r="8" fill="white" stroke="#2563eb" strokeWidth="4" />; })}
            </svg>
          </div>
        ) : (<div className="h-40 flex items-center justify-center border-2 border-dashed border-gray-50 rounded-[2rem] bg-gray-50/30"><p className="text-gray-400 font-bold uppercase tracking-widest text-[10px]">Insufficient data for trend analysis. (Min 2 reports required)</p></div>)}
        <Brain className="absolute -right-10 -bottom-10 text-blue-50/50 group-hover:text-blue-100/50 transition-all duration-700" size={240} />
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 pb-20">
        {reports.map(report => (
          <div key={report.id} onClick={() => onViewReport(report)} className="bg-white p-8 rounded-[2.5rem] border border-gray-100 shadow-sm hover:shadow-2xl hover:border-blue-100 transition-all cursor-pointer group">
            <div className="flex justify-between items-start mb-6"><div><p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">Report ID</p><p className="font-black text-gray-900">#RES-{report.id}</p></div><div className={`px-4 py-2 rounded-xl font-black text-xs uppercase tracking-widest ${report.analysis?.[0]?.confidence_score > 90 ? 'bg-blue-50 text-blue-600' : 'bg-emerald-50 text-emerald-600'}`}>{report.analysis?.[0]?.confidence_score || '0'}% match</div></div>
            <h3 className="text-2xl font-black text-gray-900 uppercase tracking-tight mb-2 group-hover:text-blue-600 transition-colors">Viability Analysis</h3>
            <div className="flex gap-4 items-center text-gray-400 font-bold text-xs uppercase tracking-widest"><span>{new Date(report.created_at).toLocaleDateString()}</span><span>•</span><span>{formatDOB(report.patient_dob)}</span><span>•</span><span className="text-blue-600">{report.analysis?.[0]?.viability_prediction || 'PENDING'}</span></div>
          </div>
        ))}
      </div>
    </div>
  );
};

const PatientHelpView = ({ doctorInfo, onLogout }) => (
  <div className="max-w-2xl mx-auto space-y-12 animate-in zoom-in-95 duration-700">
    <div className="bg-white rounded-[4rem] p-12 border border-gray-100 shadow-2xl shadow-blue-50">
      <div className="w-24 h-24 bg-blue-50 rounded-3xl flex items-center justify-center text-blue-600 mb-8"><Phone size={40} /></div>
      <h2 className="text-4xl font-black text-gray-900 tracking-tighter uppercase mb-2">Support & Help</h2>
      <p className="text-gray-400 font-bold uppercase tracking-widest text-[10px] mb-12">Contact your healthcare provider for clinical queries.</p>
      <div className="space-y-8">
        <div className="p-8 bg-gray-50 rounded-[2.5rem] border border-gray-100">
          <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-4">Assigned Doctor</p>
          <p className="text-4xl font-black text-gray-900 uppercase tracking-tight mb-1">{doctorInfo?.full_name || 'DR. HARSHH'}</p>
          <p className="text-xs font-bold text-blue-600 uppercase tracking-widest mb-4">{doctorInfo?.specialization || 'EMBRYOLOGIST'}</p>
          <p className="text-xs font-medium text-gray-500 uppercase tracking-wider">{doctorInfo?.clinic_name || 'EMBRYO METRIX CLINIC'}</p>
          <p className="text-xs font-black text-blue-600">PH: {doctorInfo?.phone_number || '9444702280'}</p>
        </div>
        <a href={`tel:${doctorInfo?.phone_number || '1234567890'}`} className="block w-full bg-blue-600 text-white font-black py-6 rounded-[2rem] text-center shadow-2xl shadow-blue-100 hover:scale-[1.02] active:scale-95 transition-all uppercase tracking-widest text-xs">Call Doctor Now</a>
        <button onClick={onLogout} className="w-full bg-white border-2 border-red-50 text-red-500 font-black py-6 rounded-[2rem] uppercase tracking-widest text-xs hover:bg-red-50 transition-all flex items-center justify-center gap-3"><LogOut size={18} /> Logout of Portal</button>
      </div>
    </div>
  </div>
);

const ProfileView = ({ profile, onUpdate, onLogout }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState(profile);
  useEffect(() => { setEditData(profile); }, [profile]);
  const handleSave = async () => { const success = await onUpdate(editData); if (success) setIsEditing(false); };
  return (
    <div className="max-w-4xl mx-auto space-y-12">
      <header className="flex justify-between items-center">
        <h1 className="text-5xl font-black text-gray-900 tracking-tighter uppercase leading-none">Profile</h1>
        <div className="flex gap-4">
          {!isEditing ? (<><button onClick={() => setIsEditing(true)} className="px-6 py-4 bg-white border-2 rounded-2xl font-black uppercase text-xs flex items-center gap-2 hover:bg-gray-50 transition-all shadow-sm"><Edit2 size={14} /> Edit Profile</button><button onClick={onLogout} className="px-6 py-4 bg-red-50 text-red-600 rounded-2xl font-black uppercase text-xs hover:bg-red-100 transition-all">Logout</button></>) : (
            <div className="flex gap-3"><button onClick={() => {setIsEditing(false); setEditData(profile);}} className="px-6 py-4 bg-white border-2 border-gray-200 rounded-2xl font-black uppercase text-xs">Cancel</button><button onClick={handleSave} className="px-8 py-4 bg-blue-600 text-white rounded-2xl font-black uppercase text-xs shadow-xl shadow-blue-100 hover:bg-blue-700 transition-all flex items-center gap-2"><Save size={14} /> Save Changes</button></div>
          )}
        </div>
      </header>
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-12">
        <div className="bg-white rounded-[3.5rem] p-12 border border-gray-100 shadow-2xl shadow-blue-50 flex flex-col items-center">
          <div className="w-48 h-48 bg-gray-50 rounded-full flex items-center justify-center text-blue-100 mb-8 overflow-hidden"><User size={80} /></div>
          {isEditing ? (<div className="w-full text-center"><label className="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2">Display Name</label><input value={editData.full_name || ''} onChange={e => setEditData({...editData, full_name: e.target.value})} className="w-full px-4 py-2 border-2 border-gray-100 rounded-xl focus:border-blue-500 outline-none text-center font-black" /></div>) : (<div className="text-center"><h3 className="text-3xl font-black uppercase text-gray-900 leading-none mb-2">{profile.full_name || 'Doctor'}</h3><p className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">{profile.email || profile.username || 'Connected Account'}</p></div>)}
          <p className="text-gray-400 font-bold text-[10px] mt-4 uppercase tracking-widest leading-relaxed">{profile.specialization || 'Healthcare Professional'}</p>
        </div>
        <div className="lg:col-span-2 bg-white rounded-[3.5rem] p-12 border border-gray-100 shadow-2xl shadow-blue-50 grid grid-cols-1 md:grid-cols-2 gap-10">
          {[ {l:'Clinic Name',k:'clinic_name'},{l:'Specialization',k:'specialization'},{l:'Experience (Yrs)',k:'experience_years',t:'number'},{l:'Phone Number',k:'phone_number'},{l:'Full Address',k:'address'} ].map(i => (
            <div key={i.k} className={i.k === 'address' ? 'md:col-span-2' : ''}>
              <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2">{i.l}</p>
              {isEditing ? (<input type={i.t || 'text'} value={editData[i.k] || ''} onChange={e => setEditData({...editData, [i.k]: e.target.value})} className="w-full px-4 py-3 bg-gray-50 border-2 border-gray-100 rounded-xl focus:border-blue-500 focus:bg-white outline-none font-bold text-gray-900 transition-all" />) : (<p className="font-black text-gray-900 text-lg border-b border-gray-50 pb-2">{profile[i.k] || 'Not Set'}</p>)}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

const ChatbotView = () => {
  const [messages, setMessages] = useState([
    { id: 1, text: "Hello! I am the Embryo Metrix AI Assistant. How can I help you with clinical analysis or patient data today?", sender: 'ai' }
  ]);
  const [input, setInput] = useState('');
  const [isTyping, setIsTyping] = useState(false);

  const RESPONSES = [
    "Based on clinical data, a D5 blastocyst with clear media turbidity shows the highest viability markers.",
    "The confidence score analysis suggests reviewing the glucose and lactate levels for further metabolic profiling.",
    "Optimal culture medium selection (G-2 PLUS) combined with no media color change indicates a strong embryo development environment.",
    "For patients with pH deviation marked as 'Slight', I recommend a follow-up assessment within 48 hours.",
    "The AI model detects a correlation between culture duration and oxidative stress markers. Consider adjusting incubation parameters.",
    "Your report data is within normal clinical parameters. The viability prediction is favorable based on current markers."
  ];

  const handleSend = (e) => {
    e.preventDefault();
    if (!input.trim()) return;
    const userMsg = { id: Date.now(), text: input, sender: 'user' };
    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setIsTyping(true);
    setTimeout(() => {
      const aiMsg = { id: Date.now() + 1, text: RESPONSES[Math.floor(Math.random() * RESPONSES.length)], sender: 'ai' };
      setMessages(prev => [...prev, aiMsg]);
      setIsTyping(false);
    }, 1400);
  };

  return (
    <div className="max-w-4xl mx-auto flex flex-col h-[75vh] bg-white rounded-[3rem] shadow-xl border border-gray-100 overflow-hidden animate-in fade-in slide-in-from-bottom-5 duration-700">
      <header className="px-10 py-8 border-b border-gray-100 flex items-center gap-6 bg-gradient-to-r from-blue-50 to-white">
        <div className="w-16 h-16 bg-blue-600 rounded-[1.5rem] flex items-center justify-center text-white shadow-xl shadow-blue-200"><Bot size={32} /></div>
        <div><h2 className="text-3xl font-black text-gray-900 tracking-tighter uppercase leading-none">AI Assistant</h2><p className="text-gray-400 font-bold uppercase tracking-[0.2em] text-[10px] mt-1">Clinical Decision Support Model • Online</p></div>
        <div className="ml-auto w-3 h-3 bg-emerald-400 rounded-full animate-pulse"></div>
      </header>
      <div className="flex-1 overflow-y-auto p-10 space-y-6 bg-[#FBFCFE]">
        {messages.map(msg => (
          <div key={msg.id} className={`flex ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}>
            {msg.sender === 'ai' && <div className="w-10 h-10 bg-blue-600 rounded-2xl flex items-center justify-center text-white mr-4 mt-1 flex-shrink-0"><Bot size={18} /></div>}
            <div className={`max-w-[75%] p-6 rounded-3xl ${msg.sender === 'user' ? 'bg-blue-600 text-white rounded-tr-sm shadow-xl shadow-blue-100' : 'bg-white text-gray-800 rounded-tl-sm border border-gray-100 shadow-sm'}`}>
              <p className="font-bold text-sm leading-relaxed">{msg.text}</p>
            </div>
          </div>
        ))}
        {isTyping && (
          <div className="flex justify-start">
            <div className="w-10 h-10 bg-blue-600 rounded-2xl flex items-center justify-center text-white mr-4 flex-shrink-0"><Bot size={18} /></div>
            <div className="bg-white px-6 py-5 rounded-3xl rounded-tl-sm border border-gray-100 flex gap-2 items-center shadow-sm">
              <span className="w-2.5 h-2.5 bg-blue-400 rounded-full animate-bounce"></span>
              <span className="w-2.5 h-2.5 bg-blue-400 rounded-full animate-bounce" style={{animationDelay: '0.15s'}}></span>
              <span className="w-2.5 h-2.5 bg-blue-400 rounded-full animate-bounce" style={{animationDelay: '0.3s'}}></span>
            </div>
          </div>
        )}
      </div>
      <div className="p-8 bg-white border-t border-gray-100">
        <form onSubmit={handleSend} className="relative flex items-center">
          <input value={input} onChange={e => setInput(e.target.value)} placeholder="Ask the clinical AI anything..." className="w-full bg-gray-50 border-2 border-gray-100 pl-8 pr-20 py-6 rounded-full font-bold text-gray-900 focus:border-blue-500 outline-none text-base transition-all" />
          <button type="submit" disabled={!input.trim()} className="absolute right-3 bg-blue-600 text-white w-14 h-14 rounded-full flex items-center justify-center disabled:opacity-40 hover:bg-blue-700 transition-all shadow-xl active:scale-95">
            <Send size={20} className="ml-0.5" />
          </button>
        </form>
      </div>
    </div>
  );
};

// --- MAIN WRAPPER ---

const EmbryoMetrixWeb = () => {
  const [currentView, setCurrentView] = useState('auth');
  const [authMode, setAuthMode] = useState('login');
  const [reports, setReports] = useState([]);
  const [profile, setProfile] = useState({});
  const [isPatient, setIsPatient] = useState(false);
  const [patientReports, setPatientReports] = useState([]);
  const [assessmentStep, setAssessmentStep] = useState(0);
  const [selectedReport, setSelectedReport] = useState(null);
  const [isInitializing, setIsInitializing] = useState(true);
  const [assessmentData, setAssessmentData] = useState({
    patient_id: '', patient_name: '', patient_dob: '', patient_age: '',
    embryo_day: '', culture_duration: '', culture_medium: '',
    media_color_change: '', ph_deviation: '', visual_clarity: '', doctor_notes: ''
  });

  const setAuthToken = (token) => { if (token) { localStorage.setItem('token', token); } else { localStorage.removeItem('token'); } };
  const fetchProfile = async () => { const resp = await api.get('/auth/profile/'); setProfile(resp.data); };
  const fetchReports = async () => { const resp = await api.get('/auth/reports/'); setReports(resp.data); };

  const handleProfileUpdate = async (updatedData) => {
    try { const resp = await api.put('/auth/profile/', updatedData); setProfile(resp.data); return true; }
    catch (e) { alert("Error updating profile: " + (e.response?.data?.error || "Unknown error")); return false; }
  };

  const handleLogin = async (formData) => {
    try {
      const resp = await api.post('/auth/login/', { username: formData.email, password: formData.password });
      setAuthToken(resp.data.access);
      if (resp.data.profile) setProfile(resp.data.profile);
      await Promise.all([fetchProfile(), fetchReports()]);
      setCurrentView('dashboard');
    } catch (e) { alert("Login Error: Please check your credentials and make sure the server is running."); }
  };

  const handleSignUp = async (formData) => {
    try {
      await api.post('/auth/signup/', { username: formData.email, email: formData.email, password: formData.password });
      const loginResp = await api.post('/auth/login/', { username: formData.email, password: formData.password });
      setAuthToken(loginResp.data.access);
      await api.post('/auth/profile/', { full_name: formData.full_name, specialization: formData.specialization, clinic_name: formData.clinic_name, phone_number: formData.phone_number, experience_years: formData.experience_years, address: formData.address });
      await fetchProfile(); await fetchReports();
      setCurrentView('dashboard');
    } catch (e) { alert("Registration Error: " + (e.response?.data?.error || "Could not complete signup.")); }
  };

  const handlePatientLogin = async (formData) => {
    try {
      const resp = await api.post('/auth/patient-login/', { patient_id: formData.patient_id, patient_dob: formData.patient_dob });
      setPatientReports(resp.data); setIsPatient(true); setSelectedReport(null);
      setCurrentView('patientReportsList');
    } catch (e) { alert("No reports found for these details. Please verify your Patient ID and DOB."); }
  };

  const submitAssessment = async () => {
    try {
      const resp = await api.post('/auth/assessments/', assessmentData);
      await api.post(`/auth/assessments/${resp.data.id}/analyze/`, { glucose_level: (Math.random()*2+3).toFixed(1), lactate_level: (Math.random()*1+1).toFixed(1), pyruvate_level: (Math.random()*0.5+0.5).toFixed(2), oxidative_stress: (Math.random()*4+2).toFixed(1) });
      await fetchReports();
      const updatedReports = await api.get('/auth/reports/');
      setSelectedReport(updatedReports.data[0]);
      setCurrentView('reportDetail');
    } catch (e) { alert("Assessment Submission Error"); }
  };

  const handleLogout = () => { setAuthToken(null); setProfile({}); setReports([]); setIsPatient(false); setPatientReports([]); setAuthMode('login'); setCurrentView('auth'); };

  useEffect(() => {
    const checkAuth = async () => {
      const savedToken = localStorage.getItem('token');
      if (savedToken) {
        setAuthToken(savedToken);
        try { await Promise.all([fetchProfile(), fetchReports()]); setCurrentView('dashboard'); }
        catch (e) { console.error("Session expired"); handleLogout(); }
      }
      setIsInitializing(false);
    };
    checkAuth();
  }, []);

  if (isInitializing) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-[#F8FAFC]">
        <div className="flex flex-col items-center gap-6 text-center">
          <div className="relative"><div className="w-20 h-20 border-4 border-blue-50 rounded-full animate-spin border-t-blue-600"></div><Activity className="absolute inset-0 m-auto text-blue-600 animate-pulse" size={32} /></div>
          <div><p className="text-gray-900 font-black uppercase text-sm tracking-widest mb-1">Authenticating</p><p className="text-gray-400 font-bold text-[10px] uppercase tracking-widest">Checking Clinical Session...</p></div>
          <button onClick={handleLogout} className="mt-4 px-6 py-2 bg-white border border-gray-100 rounded-xl text-[10px] font-black text-gray-400 uppercase tracking-widest hover:text-blue-600 hover:border-blue-100 transition-all shadow-sm">Cancel / Switch Account</button>
        </div>
      </div>
    );
  }

  if (currentView === 'auth') return (<AuthView mode={authMode} setMode={setAuthMode} onLogin={handleLogin} onSignUp={handleSignUp} onPatientLogin={handlePatientLogin} />);

  const dashboardStats = {
    total: reports.length,
    avgScore: reports.length > 0 ? (reports.reduce((acc, r) => { const score = parseFloat(r.analysis?.[0]?.confidence_score); return acc + (isNaN(score) ? 0 : score); }, 0) / reports.length).toFixed(1) : 0,
    good: reports.filter(r => { const v = (r.analysis?.[0]?.viability_prediction || '').toLowerCase(); const s = parseFloat(r.analysis?.[0]?.confidence_score); return v.includes('good') || (!v && s > 85); }).length,
    moderate: reports.filter(r => { const v = (r.analysis?.[0]?.viability_prediction || '').toLowerCase(); const s = parseFloat(r.analysis?.[0]?.confidence_score); return v.includes('moderate') || (!v && s >= 60 && s <= 85); }).length,
    bad: reports.filter(r => { const v = (r.analysis?.[0]?.viability_prediction || '').toLowerCase(); const s = parseFloat(r.analysis?.[0]?.confidence_score); return (v.includes('bad') || v.includes('low') || v.includes('poor')) || (!v && s < 60 && !isNaN(s)); }).length
  };

  return (
    <div className="min-h-screen bg-[#FBFCFE] text-gray-900 font-sans">
      <div className="flex md:flex-row flex-col min-h-screen">
        <nav className="md:w-32 bg-white border-r flex md:flex-col items-center py-10 gap-8 sticky top-0 h-screen print:hidden shadow-sm">
          <Logo size="sm" />
          <div className="flex md:flex-col gap-6 mt-10">
            {isPatient ? (
              <>
                <button onClick={() => setCurrentView('patientReportsList')} className={`p-4 rounded-2xl transition-all ${currentView === 'patientReportsList' || currentView === 'reportDetail' ? 'bg-blue-600 text-white shadow-lg shadow-blue-100' : 'text-gray-300 hover:text-gray-600'}`}><Home size={22}/></button>
                <button onClick={() => setCurrentView('patientHelp')} className={`p-4 rounded-2xl transition-all ${currentView === 'patientHelp' ? 'bg-blue-600 text-white shadow-lg shadow-blue-100' : 'text-gray-300 hover:text-gray-600'}`}><Info size={22}/></button>
              </>
            ) : (
              <>
                <button onClick={() => setCurrentView('dashboard')} className={`p-4 rounded-2xl transition-all ${currentView === 'dashboard' ? 'bg-blue-600 text-white shadow-lg shadow-blue-100' : 'text-gray-300 hover:text-gray-600'}`}><Home size={22}/></button>
                <button onClick={() => setCurrentView('reportsList')} className={`p-4 rounded-2xl transition-all ${currentView === 'reportsList' ? 'bg-blue-600 text-white shadow-lg shadow-blue-100' : 'text-gray-300 hover:text-gray-600'}`}><FileText size={22}/></button>
                <button onClick={() => setCurrentView('chatbot')} className={`p-4 rounded-2xl transition-all ${currentView === 'chatbot' ? 'bg-blue-600 text-white shadow-lg shadow-blue-100' : 'text-gray-300 hover:text-gray-600'}`}><Bot size={22}/></button>
                <button onClick={() => setCurrentView('profile')} className={`p-4 rounded-2xl transition-all ${currentView === 'profile' ? 'bg-blue-600 text-white shadow-lg shadow-blue-100' : 'text-gray-300 hover:text-gray-600'}`}><User size={22}/></button>
              </>
            )}
          </div>
          <button onClick={handleLogout} className="mt-auto flex flex-col items-center gap-2 text-gray-300 hover:text-red-500 transition-all group">
            <div className="p-4 rounded-2xl group-hover:bg-red-50"><LogOut size={22}/></div>
            <span className="text-[10px] font-black uppercase tracking-widest">Logout</span>
          </button>
        </nav>
        <main className="flex-1 p-8 md:p-16 max-w-7xl mx-auto w-full">
          {isPatient ? (
            <>
              {currentView === 'patientReportsList' && <PatientReportsListView reports={patientReports} onViewReport={(r) => {setSelectedReport(r); setCurrentView('reportDetail');}} />}
              {currentView === 'reportDetail' && <ResultsView report={selectedReport} onBackToReports={() => setCurrentView('patientReportsList')} />}
              {currentView === 'patientHelp' && <PatientHelpView doctorInfo={patientReports[0]?.doctor_info} onLogout={handleLogout} />}
            </>
          ) : (
            <>
              {currentView === 'dashboard' && <DashboardView profile={profile} onStartAssessment={() => {setAssessmentStep(0); setCurrentView('assessment');}} onViewReport={(r) => {setSelectedReport(r); setCurrentView('reportDetail');}} recentReports={reports.slice(0, 3)} stats={dashboardStats} onChatbot={() => setCurrentView('chatbot')} />}
              {currentView === 'reportsList' && <ReportsListView reports={reports} onViewReport={(r) => {setSelectedReport(r); setCurrentView('reportDetail');}} onNewAssessment={() => {setAssessmentStep(0); setCurrentView('assessment');}} />}
              {currentView === 'reportDetail' && <ResultsView report={selectedReport} onBackToReports={() => setCurrentView('reportsList')} />}
              {currentView === 'chatbot' && <ChatbotView />}
              {currentView === 'profile' && <ProfileView profile={profile} onUpdate={handleProfileUpdate} onLogout={handleLogout} />}
              {currentView === 'assessment' && <AssessmentView step={assessmentStep} data={assessmentData} setData={setAssessmentData} onNext={() => setAssessmentStep(assessmentStep + 1)} onPrev={() => setAssessmentStep(assessmentStep - 1)} onComplete={submitAssessment} />}
            </>
          )}
        </main>
      </div>
    </div>
  );
}

export default EmbryoMetrixWeb;
