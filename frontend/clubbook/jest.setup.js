jest.mock('@react-native-async-storage/async-storage', () => {
    return {
        __esModule: true,
        default: {
            clear: jest.fn().mockName("clear"),
            getAllKeys: jest.fn().mockName("getAllKeys"),
            getItem: jest.fn().mockName("getItem"),
            removeItem: jest.fn().mockName("removeItem"),
            setItem: jest.fn().mockName("setItem")
        }
    };
});

/*jest.mock('react-native/Libraries/Components/ScrollView/ScrollView', () => {
    const ScrollViewMock = jest.fn();
    ScrollViewMock.displayName = 'ScrollView';
    ScrollViewMock.defaultProps = {
        children: null,
    };
    return ScrollViewMock;
});

// Mock KeyboardAwareScrollView
jest.mock('react-native-keyboard-aware-scroll-view', () => {
    const KeyboardAwareScrollView = jest.fn().mockImplementation(({ children }) => children);
    return { KeyboardAwareScrollView };
});

jest.mock('react-native', () => {
    const reactNative = jest.requireActual('react-native');
    return Object.setPrototypeOf(
        {
            BackHandler: {
                addEventListener: jest.fn(),
                removeEventListener: jest.fn(),
            },
        },
        reactNative,
    );
});

jest.mock('@react-navigation/native', () => {
    const actualNav = jest.requireActual('@react-navigation/native');
    return {
        ...actualNav,
        useNavigation: () => ({
            navigate: jest.fn(),
        }),
    };
});*/