import React from 'react';
import renderer from 'react-test-renderer';
import App from '../App';


describe('<App />', () => {
	it('has 1 child', () => {
		const tree = renderer.create(<App />).toJSON();
		console.log("hola");
		expect(tree.children.length).toBe(1);
	});

	// Test to check if LogIn screen is rendered without header
	it('LogIn screen is rendered without header', () => {
		const tree = renderer.create(<App />).toJSON();
		const logInScreen = tree.children[1].children[0]; // Assuming NavigationContainer is second child
		expect(logInScreen.props.options.headerShown).toBe(false);
	});

	// Test to check if all the expected screens are present
	it('renders all expected screens', () => {
		const tree = renderer.create(<App />).toJSON();
		const stackScreens = tree.children[1].children[0].children;
		const screenNames = stackScreens.map(screen => screen.props.name);
		expect(screenNames).toContain('LogIn');
		expect(screenNames).toContain('AdministratorMainScreen');
		expect(screenNames).toContain('StudentMainScreen');
		expect(screenNames).toContain('TeacherMainScreen');
	});

	// Test to check if the container view has the correct style
	it('container view has correct style', () => {
		const tree = renderer.create(<App />).toJSON();
		const containerStyle = tree.props.style;
		expect(containerStyle).toMatchObject({
			flex: 1,
			backgroundColor: '#fff',
		})
	});

	// Test to check if NavigationContainer is rendered
	it('renders NavigationContainer', () => {
		const tree = renderer.create(<App />).toJSON();
		const navigationContainer = tree.children.find(child => child.type === NavigationContainer);
		expect(navigationContainer).toBeTruthy();
	});

	// Test to check if the Stack.Navigator component is rendered
	it('renders Stack.Navigator', () => {
		const tree = renderer.create(<App />).toJSON();
		const stackNavigator = tree.children[1].children[0]; // Assuming NavigationContainer is second child
		expect(stackNavigator.type).toBe('RCTView'); // As <Stack.Navigator> is a native view
	});

});
