""" push message protocol

    ANDROID push

    PUSH_NEW_THREAD = 10
    PUSH_NEW_COMMENT = 11

    PUSH_LIKE_THREAD = 20
    PUSH_LIKE_COMMENT = 21

    {
        'push_type': (int),
        'message': (String),
        'thread_id': (int),
        'comment_id': (int),
        'summary':
    }

    IOS push

    'aps': {
        'alert': (message, String),
        'badge': 1,
    },
    'thread_id': (int),         # PUSH_NEW_THREAD : no need, 나머지 전부 줄것
    'event_date': 'YYYY-mm-dd HH:MM:SS',
    'image_url': (String)

"""
