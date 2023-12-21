
let tasks = [];

function filterTasks() {
    const incompleteCheckbox = document.getElementById('incompleteCheckbox');
    const filteredTasks = incompleteCheckbox.checked
        ? tasks.filter(task => !task.status) // Show only incomplete tasks
        : tasks; // Show all tasks

    return filteredTasks;
}

async function fetchTasks(url, queryParams = {}) {
    try {
        const queryString = Object.keys(queryParams)
            .map(key => `${key}=${queryParams[key]}`)
            .join('&');

        const javaServerUrl = `http://localhost:8080/${url}?${queryString}`;
        const response = await fetch(javaServerUrl);

        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        tasks = await response.json(); // Store fetched tasks globally
        return tasks;
    } catch (error) {
        console.error(error);
    }
}

// Function to create HTML elements for a task
function createTaskElement(task) {
    const taskElement = document.createElement('div');
    taskElement.innerHTML = `
            <h3>${task.name}</h3>
            <p>Date: ${task.date}</p>
            <p>Status: ${task.status ? 'Completed' : 'Incomplete'}</p>
            <p>${task.shortDesc}</p>
            <button onclick="openTaskModal('${task.id}')">Open Description</button>
        `;
    return taskElement;
}

async function displayTasks() {
    try {
        const tasks = await fetchTasks('https://todo.doczilla.pro/api/todos');
        const taskListContainer = document.getElementById('taskList');
        taskListContainer.innerHTML = '';

        const filteredTasks = filterTasks();

        if (filteredTasks.length === 0) {
            taskListContainer.innerHTML = '<p>No tasks found.</p>';
            return;
        }

        filteredTasks.forEach(task => {
            const taskElement = createTaskElement(task);
            taskListContainer.appendChild(taskElement);
        });
    } catch (error) {
        console.error(error);
    }
}

function openTaskModal(taskId) {
    const task = tasks.find(t => t.id === taskId);
    const modalContent = document.getElementById('modalContent');
    modalContent.innerHTML = `
            <h2>${task.name}</h2>
            <p>Date: ${task.date}</p>
            <p>Status: ${task.status ? 'Completed' : 'Incomplete'}</p>
            <p>${task.fullDesc}</p>
        `;

    const taskModal = document.getElementById('taskModal');
    const overlay = document.getElementById('overlay');
    taskModal.style.display = 'block';
    overlay.style.display = 'block';

    document.body.classList.add('modal-open');
}

// Function to close task modal
function closeTaskModal() {
    const overlay = document.getElementById('overlay');
    const taskModal = document.getElementById('taskModal');
    taskModal.style.display = 'none';
    overlay.style.display = 'none';

    document.body.classList.remove('modal-open');
}

displayTasks();



// 1. Поиск задач по названию.
document.getElementById('searchButton').addEventListener('click', async () => {
    const searchQuery = document.getElementById('searchInput').value;
    const filteredTasks = await fetchTasks('/api/todos/find', { q: searchQuery });
    displayTasks(filteredTasks);
});

// 2. Календарь с возможностью выбора даты.
// ... (Implement date picker logic)

// 3. Кнопка для вывода задач на сегодняшнюю (текущую) дату.
document.getElementById('todayButton').addEventListener('click', async () => {
    const todayTasks = await fetchTasks('/api/todos/date', { from: Date.now(), to: Date.now() });
    displayTasks(todayTasks);
});

// 4. Кнопка для вывода задач на текущую неделю.
// ... (Implement logic to get tasks for the current week)

// 5. Возможность сортировать список задач по дате.
// ... (Implement sorting logic)

// 6. Возможность вывода только невыполненных задач.
// ... (Implement logic to filter only incomplete tasks)

// 7. Возможность открывать полное описание задачи (например, в модальном окне).
// ... (Implement logic to open full description)

// 8. В календаре можно выбрать диапазон дат.
// ... (Implement date range selection logic)

// 9. Поиск с выпадающим списком найденных задач, по нажатии на элементы которого открывается полное описание задачи.
// ... (Implement dropdown search with full description)


displayTasks(mockTasks);