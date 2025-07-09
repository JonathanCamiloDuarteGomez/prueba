 import getDatos from "./getDatos.js";
 // Variables globales
        let currentTab = 'login';

        // Función para cambiar entre pestañas
        function switchTab(tab) {
            currentTab = tab;
            
            // Actualizar botones de pestañas
            document.querySelectorAll('.tab-button').forEach(btn => {
                btn.classList.remove('active');
            });
            event.target.classList.add('active');
            
            // Mostrar/ocultar formularios
            if (tab === 'login') {
                document.getElementById('loginForm').style.display = 'block';
                document.getElementById('registerForm').style.display = 'none';
            } else {
                document.getElementById('loginForm').style.display = 'none';
                document.getElementById('registerForm').style.display = 'block';
            }
            
            // Limpiar mensajes
            clearMessages();
        }

        // Función para mostrar mensajes de error
        function showError(message) {
            const errorDiv = document.getElementById('errorMessage');
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
            document.getElementById('successMessage').style.display = 'none';
        }

        // Función para mostrar mensajes de éxito
        function showSuccess(message) {
            const successDiv = document.getElementById('successMessage');
            successDiv.textContent = message;
            successDiv.style.display = 'block';
            document.getElementById('errorMessage').style.display = 'none';
        }

        // Función para limpiar mensajes
        function clearMessages() {
            document.getElementById('errorMessage').style.display = 'none';
            document.getElementById('successMessage').style.display = 'none';
        }

        // Validación de email
        function isValidEmail(email) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(email);
        }
        // Validación de nombre de usuario
        function isValidNombre(nombre) {
            return nombre.trim().length >= 5; // Al menos 5 caracteres
        }

        // Validación de contraseña
        function isValidPassword(password) {
            return password.length >= 6;
        }

        // Función para simular loading
        function setLoading(isLoading) {
            const container = document.querySelector('.login-container');
            if (isLoading) {
                container.classList.add('loading');
            } else {
                container.classList.remove('loading');
            }
        }

        // Manejo del formulario de login
        document.getElementById('loginForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            clearMessages();
            
            const nombre = document.getElementById('loginNombre').value.toLowerCase();
            const password = document.getElementById('loginPassword').value;
            console.log(nombre);
            
            // Validaciones básicas
            if (!isValidNombre(nombre)) {
                showError('Nombre de usuario inválido');
                return;
            }
            
            if (!isValidPassword(password)) {
                showError('La contraseña debe tener al menos 6 caracteres');
                return;
            }
            
            setLoading(true);
            
            try {
                // llamada a API
                await authenticateUser(nombre, password);
                
                // Aquí iría tu lógica de autenticación real
                // const response = await fetch('/api/login', { ... });
                // localStorage 
                localStorage.setItem('usuario', nombre);
                // Simulación de login exitoso
                showSuccess('¡Inicio de sesión exitoso! Redirigiendo...');
                
                setTimeout(() => {
                    // Redirigir a la página principal
                    window.location.href = '/usuario.html'; // Cambia por tu URL
                }, 2000);
                
            } catch (error) {
                showError('Error al iniciar sesión. Por favor intenta nuevamente.');
            } finally {
                setLoading(false);
            }
        });

        // Manejo del formulario de registro
        document.getElementById('registerForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            clearMessages();
            
            const name = document.getElementById('registerName').value;
            const email = document.getElementById('registerEmail').value;
            const password = document.getElementById('registerPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            // Validaciones
            if (name.trim().length < 2) {
                showError('El nombre debe tener al menos 2 caracteres');
                return;
            }
            
            if (!isValidEmail(email)) {
                showError('Por favor ingresa un correo electrónico válido');
                return;
            }
            
            if (!isValidPassword(password)) {
                showError('La contraseña debe tener al menos 6 caracteres');
                return;
            }
            
            if (password !== confirmPassword) {
                showError('Las contraseñas no coinciden');
                return;
            }
            
            setLoading(true);
            
            try {
                // Simular llamada a API
                await new Promise(resolve => setTimeout(resolve, 2000));
                
                // Aquí iría tu lógica de registro real
                // const response = await fetch('/api/register', { ... });
                
                // Simulación de registro exitoso
                showSuccess('¡Cuenta creada exitosamente! Puedes iniciar sesión ahora.');
                
                setTimeout(() => {
                    switchTab('login');
                    document.getElementById('loginEmail').value = email;
                }, 2000);
                
            } catch (error) {
                showError('Error al crear la cuenta. Por favor intenta nuevamente.');
            } finally {
                setLoading(false);
            }
        });

        // Limpiar mensajes cuando el usuario empiece a escribir
        document.querySelectorAll('input').forEach(input => {
            input.addEventListener('input', clearMessages);
        });
        //funcion para conectarse con el servidor y enviar los datos para la autenticación
        async function authenticateUser(nombre, password) {
            try {
               const response = await fetch('http://localhost:8080/api/usuarios/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                    },
                body: JSON.stringify({ nombre: nombre, contrasena: password })
                });

                
                if (!response.ok) {
                    throw new Error('Error en la autenticación');
                }
                
                const data = await response.json();
                return data; // Retorna los datos del usuario
            } catch (error) {
                console.error('Error al autenticar:', error);
                throw error;
            }
        }

       
        // Hacer accesible desde el HTML
        window.switchTab = switchTab;
