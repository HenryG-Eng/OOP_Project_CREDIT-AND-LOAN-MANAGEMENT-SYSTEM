# MANUAL DE INSTALACIÓN
## FinCredit — Credit & Loan Management System
**Proyecto Final POO | Autores: Cristian Castro y Henry Garrido | Versión: 1.0 | Fecha: 2026**

---

## TABLA DE CONTENIDO

1. Requisitos del sistema
2. Instalación del entorno de desarrollo (JDK)
3. Instalación de Eclipse IDE
4. Importación del proyecto en Eclipse
5. Verificación de la estructura del proyecto
6. Compilación y ejecución
7. Exportar y ejecutar como archivo .jar
8. Solución de problemas comunes

---

## 1. REQUISITOS DEL SISTEMA

Antes de instalar el proyecto, asegúrese de que su computador cumple con los siguientes requisitos mínimos.

### Hardware

| Componente | Mínimo |
|---|---|
| Procesador | 1 GHz o superior |
| Memoria RAM | 2 GB (4 GB recomendado) |
| Espacio en disco | 500 MB libres |
| Pantalla | Resolución mínima 1024 × 768 |

### Software

| Componente | Versión requerida |
|---|---|
| Sistema Operativo | Windows 10/11, macOS 11+, Ubuntu 20.04+ |
| Java Development Kit (JDK) | JDK 11 o superior (recomendado: JDK 17 LTS) |
| IDE (entorno de desarrollo) | Eclipse IDE 2022-06 o superior |
| Archivo fuente | `POO_Project_CREDIT-AND-LOAN-MANAGEMENT-SYSTEM-main.zip` |

> **Nota:** El proyecto no requiere base de datos externa ni servidor de aplicaciones. Toda la información se gestiona en memoria durante la ejecución.

---

## 2. INSTALACIÓN DEL ENTORNO DE DESARROLLO (JDK)

### Paso 1 — Descargar JDK 17

1. Abra su navegador y vaya a:
   `https://www.oracle.com/java/technologies/downloads/#java17`
2. Seleccione la pestaña de su sistema operativo (Windows / macOS / Linux).
3. Descargue el instalador correspondiente.

### Paso 2 — Instalar JDK (Windows)

1. Ejecute el archivo descargado (p. ej. `jdk-17_windows-x64_bin.exe`).
2. Haga clic en **Next** en todas las pantallas y luego en **Install**.
3. Espere a que finalice la instalación y haga clic en **Close**.

### Paso 3 — Configurar la variable de entorno JAVA_HOME (Windows)

1. Haga clic derecho en **Este equipo** → **Propiedades** → **Configuración avanzada del sistema**.
2. Haga clic en **Variables de entorno**.
3. En "Variables del sistema", haga clic en **Nueva** e ingrese:
   - Nombre de la variable: `JAVA_HOME`
   - Valor: `C:\Program Files\Java\jdk-17` (verifique la ruta exacta en su equipo)
4. Busque la variable `Path` en la lista, haga clic en **Editar** → **Nuevo** y agregue:
   `%JAVA_HOME%\bin`
5. Acepte todos los cuadros de diálogo.

### Paso 4 — Verificar la instalación de Java

Abra una terminal (CMD en Windows / Terminal en macOS o Linux) y ejecute:

```
java -version
```

Debería ver una salida similar a:

```
java version "17.0.x" 2024-xx-xx LTS
Java(TM) SE Runtime Environment ...
```

Si aparece ese mensaje, Java está correctamente instalado.

---

## 3. INSTALACIÓN DE ECLIPSE IDE

### Paso 1 — Descargar Eclipse

1. Vaya a `https://www.eclipse.org/downloads/`
2. Descargue **Eclipse IDE for Java Developers**.

### Paso 2 — Instalar Eclipse

1. Ejecute el instalador descargado.
2. Seleccione la opción **Eclipse IDE for Java Developers**.
3. Verifique que la ruta de instalación sea correcta y haga clic en **Install**.
4. Acepte las licencias y espere a que finalice.
5. Haga clic en **Launch** para iniciar Eclipse por primera vez.
6. Elija una carpeta como espacio de trabajo (workspace) y haga clic en **Launch**.

---

## 4. IMPORTACIÓN DEL PROYECTO EN ECLIPSE

### Paso 1 — Descomprimir el archivo del proyecto

1. Localice el archivo `POO_Project_CREDIT-AND-LOAN-MANAGEMENT-SYSTEM-main.zip`.
2. Haga clic derecho sobre él y seleccione **Extraer todo** (Windows) o **Abrir con → Archivador** (Linux/macOS).
3. Elija una carpeta de destino de fácil acceso, por ejemplo: `C:\Proyectos\FinCredit\`
4. Confirme que la extracción generó la carpeta:
   `POO_Project_CREDIT-AND-LOAN-MANAGEMENT-SYSTEM-main\`

### Paso 2 — Importar en Eclipse

1. En Eclipse, vaya al menú: **File → Import...**
2. En el cuadro de diálogo, expanda **General** y seleccione **Existing Projects into Workspace**.
3. Haga clic en **Next**.
4. En "Select root directory", haga clic en **Browse** y navegue hasta la carpeta extraída en el paso anterior.
5. Eclipse detectará el proyecto automáticamente y lo mostrará con el nombre `POO_Project_CREDIT-AND-LOAN-MANAGEMENT-SYSTEM-main`.
6. Asegúrese de que esté marcado el checkbox junto al proyecto.
7. Haga clic en **Finish**.

---

## 5. VERIFICACIÓN DE LA ESTRUCTURA DEL PROYECTO

Tras importar, en el panel **Package Explorer** de Eclipse debe aparecer la siguiente estructura:

```
POO_Project_CREDIT-AND-LOAN-MANAGEMENT-SYSTEM-main/
│
├── src/
│   ├── Images/
│   │   └── Logoprot.png          ← Logo de la aplicación
│   │
│   ├── com.fincredit.gui/
│   │   ├── Main.java             ← Punto de entrada (main)
│   │   ├── MainFrame.java        ← Ventana principal
│   │   ├── DashboardPanel.java   ← Panel Dashboard
│   │   ├── ClientsPanel.java     ← Panel Lista de clientes
│   │   ├── LoansPanel.java       ← Panel Lista de préstamos
│   │   ├── NewClientPanel.java   ← Formulario nuevo cliente
│   │   └── NewLoanPanel.java     ← Formulario nuevo préstamo
│   │
│   ├── com.fincredit.interfaces/
│   │   ├── IEvaluable.java       ← Interfaz de evaluación
│   │   └── ILoanable.java        ← Interfaz de producto
│   │
│   ├── com.fincredit.logic/
│   │   ├── CreditEvaluator.java  ← Evaluador de crédito
│   │   └── LoanProductFactory.java ← Fábrica de productos
│   │
│   ├── com.fincredit.model/
│   │   ├── Person.java           ← Clase abstracta base
│   │   ├── Client.java           ← Modelo de cliente
│   │   ├── LoanOfficer.java      ← Modelo oficial de crédito
│   │   ├── Loan.java             ← Modelo de préstamo
│   │   ├── LoanProduct.java      ← Clase abstracta producto
│   │   ├── AmortizationRow.java  ← Fila de amortización
│   │   ├── EvaluationResult.java ← Resultado de evaluación
│   │   ├── ConsumerLoan.java     ← Préstamo consumo
│   │   ├── EducationalLoan.java  ← Préstamo educativo
│   │   ├── MortgageLoan.java     ← Préstamo hipotecario
│   │   └── FreeInvestmentLoan.java ← Préstamo libre inversión
│   │
│   ├── com.fincredit.registry/
│   │   └── LoanRegistry.java     ← Repositorio central
│   │
│   └── module-info.java
│
├── .classpath
└── .project
```

Si algún paquete aparece con error (ícono rojo), continúe con el siguiente paso.

---

## 6. COMPILACIÓN Y EJECUCIÓN

### Paso 1 — Verificar la versión de Java del proyecto

1. Haga clic derecho sobre el proyecto en el Package Explorer.
2. Seleccione **Properties → Java Build Path → Libraries**.
3. Verifique que aparezca **JRE System Library [JavaSE-11]** o superior.
4. Si aparece con error, haga clic en **Edit**, seleccione **Workspace default JRE** y confirme.

### Paso 2 — Limpiar y compilar el proyecto

1. En el menú de Eclipse: **Project → Clean...**
2. Seleccione el proyecto y haga clic en **Clean**.
3. Eclipse compilará automáticamente todos los archivos `.java`.
4. En la pestaña **Problems** (parte inferior), no deben aparecer errores en rojo.

### Paso 3 — Ejecutar la aplicación

**Opción A — Desde Eclipse:**
1. En el Package Explorer, abra el paquete `com.fincredit.gui`.
2. Haga clic derecho sobre `Main.java`.
3. Seleccione **Run As → Java Application**.
4. La ventana de FinCredit se abrirá automáticamente.

**Opción B — Desde línea de comandos:**
1. Abra una terminal en la carpeta raíz del proyecto.
2. Compile:
   ```
   javac -sourcepath src -d bin src/com/fincredit/gui/Main.java
   ```
3. Ejecute:
   ```
   java -cp bin com.fincredit.gui.Main
   ```

---

## 7. EXPORTAR Y EJECUTAR COMO ARCHIVO .JAR

El archivo `.jar` es un ejecutable empaquetado que permite correr la aplicación **sin necesidad de tener Eclipse abierto**, solo con Java instalado.

### Paso 1 — Exportar el .jar desde Eclipse

1. En Eclipse, haga clic derecho sobre el proyecto en el Package Explorer.
2. Seleccione **Export...**
3. En el cuadro de diálogo, expanda **Java** y seleccione **Runnable JAR file**.
4. Haga clic en **Next**.
5. Configure las opciones:
   - **Launch configuration:** seleccione `Main - OOP_Project_CREDIT-AND-LOAN-MANAGEMENT-SYSTEM-main`
     *(Si no aparece, primero ejecute el proyecto al menos una vez con Run As → Java Application)*
   - **Export destination:** elija la carpeta donde quiere guardar el archivo y nómbrelo, por ejemplo: `FinCredit.jar`
   - **Library handling:** seleccione `Extract required libraries into generated JAR`
6. Haga clic en **Finish**.
7. Si aparece una advertencia sobre archivos duplicados, haga clic en **OK**.

El archivo `FinCredit.jar` quedará guardado en la carpeta que eligió.

### Paso 2 — Ejecutar el .jar

**Opción A — Doble clic (Windows / macOS):**
1. Navegue hasta la carpeta donde guardó `FinCredit.jar`.
2. Haga doble clic sobre el archivo.
3. La ventana de FinCredit se abrirá automáticamente.

> Si el doble clic no funciona en Windows, haga clic derecho → **Abrir con → Java(TM) Platform SE Binary**.

**Opción B — Desde la terminal (cualquier sistema operativo):**
1. Abra una terminal (CMD, PowerShell o Terminal).
2. Navegue hasta la carpeta donde está el `.jar`:
   ```
   cd C:\Ruta\Donde\Guardo\El\Jar
   ```
3. Ejecute el siguiente comando:
   ```
   java -jar FinCredit.jar
   ```
4. La ventana de FinCredit se abrirá inmediatamente.

### Verificar que Java puede ejecutar el .jar

Si al ejecutar el `.jar` aparece un error, verifique que Java esté correctamente instalado:
```
java -version
```
Debe mostrar la versión 11 o superior. Si no aparece, revise el **Paso 3** de la sección de instalación del JDK.

> **Nota importante:** El archivo `.jar` y la carpeta `Images/` (que contiene el logo) deben estar en la misma carpeta para que el logo de la aplicación cargue correctamente. Si solo copia el `.jar` sin la carpeta de imágenes, la aplicación funcionará pero el logo no se mostrará.

---

## 8. SOLUCIÓN DE PROBLEMAS COMUNES

| Problema | Causa probable | Solución |
|---|---|---|
| Error: "JRE not found" | JDK no configurado en Eclipse | Ir a **Window → Preferences → Java → Installed JREs** y agregar el JDK instalado |
| Error: "Cannot find symbol" en compilación | Versión de Java incorrecta | Verificar que el proyecto use Java 11 o superior en **Project Properties** |
| La ventana no abre / error NullPointerException en logo | Imagen `Logoprot.png` no encontrada | Verificar que `src/Images/Logoprot.png` exista dentro del proyecto |
| Error al importar: "Project already exists" | El proyecto ya fue importado antes | Eliminar el proyecto del workspace (sin borrar archivos) y volver a importar |
| Pantalla en blanco al iniciar | Resolución de pantalla muy baja | Verificar que la resolución sea mínimo 1024×768 y que el tamaño de fuente del sistema sea 100% |
| La aplicación cierra sola | Error en tiempo de ejecución | Revisar la pestaña **Console** en Eclipse para ver el stack trace del error |

---

*Manual de Instalación — FinCredit v1.0 — Proyecto Final POO*
